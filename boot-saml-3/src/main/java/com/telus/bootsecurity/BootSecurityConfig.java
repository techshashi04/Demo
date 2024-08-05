package com.telus.bootsecurity;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collection;
import java.util.List;

import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider.ResponseToken;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Change : With Spring Boot 3.1 and Spring Security 6.1
 * - We don't need to extend WebSecurityConfigurerAdapter anymore. We now use a
 *   @Bean to return a SecurityFilterChain
 * - Some methods in HttpSecurity has been deprecated. The new methods has configurers
 *   as a parameter
 * - Also, Spring Boot SAML integration now uses OpenSAML Version 4 because of which
 *   there are changes to classes being used and hence the pom.xml file now includes
 *   the opensaml Version 4 jar files.
 */
@Configuration
public class BootSecurityConfig {
	
	@Autowired
	RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;
	
	// change : Return a Spring Bean SecurityFilterChain
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// Create a filter to handle generation of SAML Metadata file
		Saml2MetadataFilter filter = new Saml2MetadataFilter(
				(RelyingPartyRegistrationResolver)new DefaultRelyingPartyRegistrationResolver(this.relyingPartyRegistrationRepository),
				new OpenSamlMetadataResolver());

		// Actual code to extract the Authorities (or roles) from the Assertion
		Converter<Assertion, Collection<? extends GrantedAuthority>> authoritiesExtractor =  assertion -> {
			
			List<SimpleGrantedAuthority> userRoles 
				= assertion.getAttributeStatements().stream()
											.map(AttributeStatement::getAttributes)
											.flatMap(Collection::stream)
											.filter(attr -> "groups".equalsIgnoreCase(attr.getName()))
											.map(Attribute::getAttributeValues)
											.flatMap(Collection::stream)
											.map(xml -> new SimpleGrantedAuthority("ROLE_" + xml.getDOM().getTextContent()))
											.toList();
			return userRoles;
		};
		
		// change : Create a version of SAML Authenticator Provider which will convert the 
		// "groups" claim into Authorities in the SAML 2 Authentication object
		Converter<ResponseToken, Saml2Authentication> authConvertor 
			= OpenSaml4AuthenticationProvider.createDefaultResponseAuthenticationConverter();
		OpenSaml4AuthenticationProvider samlAuthProv = new OpenSaml4AuthenticationProvider();
		samlAuthProv.setResponseAuthenticationConverter((responseToken) -> {
			
			// Make sure the authorities are set in the SAML Authentication
			Saml2Authentication authentication = authConvertor.convert(responseToken);
			Assertion assertion = responseToken.getResponse().getAssertions().get(0);
			AuthenticatedPrincipal principal = (AuthenticatedPrincipal) authentication.getPrincipal();
			Collection<? extends GrantedAuthority> authorities = authoritiesExtractor.convert(assertion);
			return new Saml2Authentication(principal, authentication.getSaml2Response(), authorities);
		});		

		
		http
			.saml2Login(withDefaults())
			.saml2Logout(withDefaults())
			.authenticationProvider(samlAuthProv) // change : register the new SAML Auth provider
			.addFilterBefore(filter, Saml2WebSsoAuthenticationFilter.class)
			.authorizeHttpRequests(authorize -> 
				authorize
					.requestMatchers("/", "/carsonline", "/buy/**", "/user").hasAnyRole("cars.user","cars.admin")
					.requestMatchers("/edit/**").hasAnyRole("cars.admin")
					.anyRequest().authenticated());
 
		// change : return the SecurityFilterChain
		return http.build();
	}


}

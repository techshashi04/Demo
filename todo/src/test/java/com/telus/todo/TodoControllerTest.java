package com.telus.todo;

import com.telus.todo.controller.TodoController;
import com.telus.todo.model.Todo;
import com.telus.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc  mockMvc;

    @MockBean
    private TodoService todoService;

    /*@Test
    public void createTodoTest()throws  Exception{
        Todo todo =new Todo();
        todo.setDescription("XYZ");
        todo.setStatus("completed");

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\": \"XYZ\",\"{\"description\": \"XYZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("XYZ"))
                .andExpect(jsonPath("$.status").value("completed"));

    }*/

}

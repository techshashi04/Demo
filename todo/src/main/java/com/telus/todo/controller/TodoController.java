package com.telus.todo.controller;

import com.telus.todo.model.Todo;
import com.telus.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService  todoService;

    @PostMapping
    public Todo createTodo(@RequestBody Todo  todo){
        return todoService.createTodo(todo);
    }

    @GetMapping
    public List<Todo> getAllTodos(){
        return todoService.getAllTodos();
    }
    @GetMapping
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id){
        Optional<Todo> todo= todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todos){
        try {
            Todo todo =todoService.updateTodo(id,todos);
            return ResponseEntity.ok(todo);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id){
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}

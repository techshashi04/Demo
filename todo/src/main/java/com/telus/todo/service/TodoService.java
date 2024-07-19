package com.telus.todo.service;

import com.telus.todo.model.Todo;
import com.telus.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository  todoRepository;

    public Todo createTodo(Todo todo){
        return todoRepository.save(todo);
    }

    public List<Todo> getAllTodos(){
        return todoRepository.findAll();
    }


    public Optional<Todo> getTodoById(Long id){
        return todoRepository.findById(id);
    }


    public Todo updateTodo(Long id, Todo todos){
        return todoRepository.findById(id).map(todo ->{todo.setDescription(todos.getDescription());
        todo.setStatus(todos.getStatus());
        return todoRepository.save(todo);
        }).orElseThrow(() -> new RuntimeException("No todo found"));
    }

    public void deleteTodo(Long id){
        todoRepository.deleteById(id);
    }


}

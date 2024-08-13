package Growup.spring.todoList.application;

import Growup.spring.todoList.domain.TodoList;
import java.util.List;

public interface TodoListRepository {
    public List<TodoList> findByUserId(Long userId);
    public TodoList findById(Long todoListId);
    public TodoList save(TodoList todoList);


}

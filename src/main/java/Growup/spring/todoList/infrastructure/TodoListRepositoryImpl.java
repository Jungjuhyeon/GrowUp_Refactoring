package Growup.spring.todoList.infrastructure;

import Growup.spring.constant.handler.TodoListHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.todoList.application.TodoListRepository;
import Growup.spring.todoList.domain.TodoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class TodoListRepositoryImpl implements TodoListRepository {

    private final TodoListJpaRepository todoListJpaRepository;
    @Override
    public List<TodoList> findByUserId(Long userId){
        return todoListJpaRepository.findByUserId(userId);
    }

    @Override
    public TodoList findById(Long todoListId){
        return todoListJpaRepository.findById(todoListId).orElseThrow(()-> new TodoListHandler(ErrorStatus.TODOLIST_NOT_FOUND));
    }
    @Override
    public TodoList save(TodoList todoList){
        return todoListJpaRepository.save(todoList);
    }


}

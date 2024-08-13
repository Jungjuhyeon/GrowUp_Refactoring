package Growup.spring.todoList.infrastructure;

import Growup.spring.todoList.domain.TodoList;
import Growup.spring.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoListJpaRepository extends JpaRepository<TodoList, Long> {

    List<TodoList> findByUserId(Long userId);
}

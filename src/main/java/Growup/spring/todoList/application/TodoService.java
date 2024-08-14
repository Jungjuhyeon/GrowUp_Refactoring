package Growup.spring.todoList.application;

import Growup.spring.todoList.converter.TodoListConverter;
import Growup.spring.todoList.dto.TodoDtoListReq;
import Growup.spring.todoList.dto.TodoDtoListRes;
import Growup.spring.todoList.domain.TodoList;
import Growup.spring.user.application.UserRepository;
import Growup.spring.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService{

    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    //todolist 등록
    @Transactional
    public TodoList todoListEnroll(TodoDtoListReq.todoEnrollReq request,Long userId){
        User user = userRepository.findById(userId);
        return todoListRepository.save(TodoList.create(user.getId(),request));
    }

    //todolist 조회 - 오늘 날짜만 조회
    public List<TodoDtoListRes.todoInquiryRes> todoListInquiry(Long userId){
        User user = userRepository.findById(userId);
        LocalDate today = LocalDate.now();

        return todoListRepository.findByUserId(userId)
                .stream()
                .filter(todoList -> todoList.getCreatedAt().toLocalDate().isEqual(today))  // Filtering 오늘날짜만 조회
                .map(TodoListConverter::todoInquiryRes)
                .collect(Collectors.toList());
    }

    //todoList 내용 수정
    @Transactional
    public void todoListCommentModify(Long todoListId,TodoDtoListReq.todoCommentModifyReq request){
        TodoList todoList = todoListRepository.findById(todoListId);

        todoList.updateComment(request.getComment());
        todoListRepository.save(todoList);
    }

    //투두리스트 상태 수정
    @Transactional
    public void modifyservice(Long todoListId){
        TodoList todoList = todoListRepository.findById(todoListId);

        todoList.updateStatus();

        todoListRepository.save(todoList);
    }

}

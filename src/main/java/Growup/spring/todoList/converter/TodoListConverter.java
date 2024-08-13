package Growup.spring.todoList.converter;

import Growup.spring.todoList.dto.TodoDtoListRes;
import Growup.spring.todoList.domain.TodoList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodoListConverter {


    //todolist 등록 응답
    public static TodoDtoListRes.todoEnrollRes todoListDtoRes(TodoList todoList){
        return TodoDtoListRes.todoEnrollRes .builder()
                .userId(todoList.getId())
                .createAt(todoList.getCreatedAt())
                .build();
    }

    //todolist 조회 리스트
    public static TodoDtoListRes.todoInquiryRes todoInquiryRes(TodoList todoList) {
        return TodoDtoListRes.todoInquiryRes.builder()
                .todoListId(todoList.getId())
                .comment(todoList.getComment())
                .status(todoList.getStatus())
                .build();
    }
    //todolist 조회 리스트+ Userid 포함
    public static TodoDtoListRes.todoResultInquiryRes todoResultInquiryRes(Long userId,List<TodoDtoListRes.todoInquiryRes> todoList) {
        return TodoDtoListRes.todoResultInquiryRes.builder()
                .userId(userId)
                .todoListList(todoList)
                .build();
    }
}

package Growup.spring.todoList.presentation;


import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.todoList.application.TodoService;
import Growup.spring.todoList.converter.TodoListConverter;
import Growup.spring.todoList.dto.TodoDtoListReq;
import Growup.spring.todoList.dto.TodoDtoListRes;
import Growup.spring.todoList.domain.TodoList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/todo")
public class TodoListController {

    private final TodoService todoListService;
    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.13 수정자(리팩토링) : 정주현
     * 투두리스트 작성
     */
    @ResponseBody
    @PostMapping("/enroll")
    public ApiResponse<TodoDtoListRes.todoEnrollRes> todoListEnroll(@AuthenticationPrincipal Long userId, @RequestBody @Valid TodoDtoListReq.todoEnrollReq request){
        TodoList todoList = todoListService.todoListEnroll(request,userId);
        return ApiResponse.onSuccess(TodoListConverter.todoListDtoRes(todoList));
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.13 수정자(리팩토링) : 정주현
     * 투두리스트 조회
     */
    @GetMapping("/inquiry")
    public ApiResponse<TodoDtoListRes.todoResultInquiryRes> todoListInquiry(@AuthenticationPrincipal Long userId){
        return ApiResponse.onSuccess(TodoListConverter.todoResultInquiryRes(userId,todoListService.todoListInquiry(userId)));
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.13 수정자(리팩토링) : 정주현
     * 투두리스트 내용 수정
     */
    @PatchMapping("/modify-comment")
    public ApiResponse<SuccessStatus> todoListCommentModify(@RequestParam Long todoListId,@RequestBody TodoDtoListReq.todoCommentModifyReq request){
        todoListService.todoListCommentModify(todoListId,request);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }
    /**
     * 24.01.29 작성자 : 정주현
     * 24.08.13 수정자(리팩토링) : 정주현
     * 투두리스트 체크(상태) 수정
     */
    @PatchMapping("/modify-status")
    public ApiResponse<SuccessStatus> modifyStatus(@RequestParam Long todoListId){
        todoListService.modifyservice(todoListId);
        return ApiResponse.onSuccessWithoutResult(SuccessStatus._OK);
    }



}

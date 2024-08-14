package Growup.spring.todoList.domain;

import Growup.spring.constant.entity.BaseEntity;
import Growup.spring.todoList.domain.Enum.TodoStatus;
import Growup.spring.todoList.dto.TodoDtoListReq;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodoList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Enumerated(EnumType.STRING)
//  @Column(columnDefinition = "VARCHAR(10) DEFAULT 'NONACTIVE'")
    private TodoStatus status;

//
// @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    private Long userId;

    //== 생성자 메소드 ==//
    public static TodoList create(Long userId, TodoDtoListReq.todoEnrollReq request){
        return TodoList.builder()
                .userId(userId)
                .comment(request.getComment())
                .status(TodoStatus.NON_COMPLETED) //초기값은 수행되지 않게 처리하기 위해
                .build();
    }
    //== 내용 수정 ==//
    public void updateComment(String comment){
        this.comment = comment;
    }

    //== 상태 수정 ==//
    public void updateStatus(){
        if(this.getStatus() == TodoStatus.COMPLETED){
            this.status = TodoStatus.NON_COMPLETED;
        }
        else{
            this.status = TodoStatus.COMPLETED;
        }
    }



}

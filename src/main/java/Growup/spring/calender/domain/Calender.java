package Growup.spring.calender.domain;

import Growup.spring.calender.domain.Enum.CalenderStatus;
import Growup.spring.calender.dto.CalenderDtoReq;
import Growup.spring.constant.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Calender extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String comment;

    private LocalDate day;

    @Enumerated(EnumType.STRING)
    private CalenderStatus status;

    @Column(name ="user_id")
    private Long userId;


    //== 생성 메소드 ==//
    public static Calender create(Long userId, CalenderDtoReq.calenderEnroll request){
        return Calender.builder()
                .comment(request.getComment())
                .day(request.getDay())
                .status(CalenderStatus.UNCHECKED)
                .userId(userId)
                .build();
    }

    //== 내용 수정 메소드 ==//
    public void updateComment(String comment){
        this.comment = comment;
    }

    //== 상태 반전 메소드 ==//
    public void toggleStatus(){
        if (this.status == CalenderStatus.CHECKED ){
            this.status = CalenderStatus.UNCHECKED;
        } else{
            this.status = CalenderStatus.CHECKED;
        }
    }


}

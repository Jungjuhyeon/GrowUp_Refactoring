//package Growup.spring.calender.domain;
//
//import Growup.spring.calender.domain.Enum.CalenderColorStatus;
//import Growup.spring.constant.entity.BaseEntity;
//import Growup.spring.user.domain.User;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//
//import java.time.LocalDate;
//
//@Entity
//@Getter
//@Setter
//@DynamicUpdate
//@DynamicInsert
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//public class CalenderColor extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private LocalDate day;
//
//    @Enumerated(EnumType.STRING)
//    private CalenderColorStatus color;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name ="userId")
//    private User user;
//
//
//}

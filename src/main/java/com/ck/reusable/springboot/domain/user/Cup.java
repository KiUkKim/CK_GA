//package com.ck.reusable.springboot.domain.user;
//
//import lombok.*;
//import org.hibernate.annotations.ColumnDefault;
//
//import javax.persistence.*;
//
//@Getter
//@NoArgsConstructor
//@Entity
//@Data
//@Table(name="Cup")
//public class Cup {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long cup_uid;
//
//    @Column(name = "total_cnt", nullable = false)
//    private Integer total_cnt;
//
//    @Column(name = "state", nullable = false)
//    private Integer state;
//
//    @OneToOne
//    @JoinColumn(name = "Store_PK")
//    private Store store;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//}

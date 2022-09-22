package com.ck.reusable.springboot.domain.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Table(name="rental_history")
public class rental_history {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rental_id;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security시 발생 )
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cupUid")
    @JsonBackReference(value = "relation-Cup-rental_history")
    private Cup cup;

    @Column
    private LocalDateTime rentalAT;

    // insertable 써줘야지 insert 자동으로 안함!!
    @Column(nullable = true, insertable = false)
    private LocalDateTime returnAT;

    // check = 0 <<< 현재 빌리고 있는 컵, check = 1 << 과거의 컵들
    @Column(nullable = false)
    private Integer checkValue;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security 시 발생 )
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonBackReference(value = "relation-User-rental_history")
    private User user;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security 시 발생 )
    @JsonBackReference(value = "relation-StoreInfo-rental_history")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId")
    private StoreInfo store;

    @Builder
    public rental_history(LocalDateTime rentalAT, Cup cup, User user, StoreInfo store, Integer checkValue)
    {
        this.rentalAT = rentalAT;
        this.user = user;
        this.cup = cup;
        this.store = store;
        this.checkValue = checkValue;
    }
}

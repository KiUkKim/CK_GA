package com.ck.reusable.springboot.domain.History;

import com.ck.reusable.springboot.domain.Cup.Cup;
import com.ck.reusable.springboot.domain.Store.StoreInfo;
import com.ck.reusable.springboot.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Setter // 나중에 삭제해야함 테스트용때문에 넣어둠
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
    @JoinColumn(name = "goodAttitudeCup_Uid")
    @JsonBackReference(value = "relation-Cup-rental_history")
    private Cup cup;

    @Column
    private LocalDateTime rentalAT;

    // insertable 써줘야지 insert 자동으로 안함!!
//    @Column(nullable = true, insertable = false)
//    private LocalDateTime returnAT;

    // check = 0 <<< 현재 빌리고 있는 컵, check = 1 << 과거의 컵들
    @Column(nullable = false)
    private Integer checkValue;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security 시 발생 )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @JsonBackReference(value = "relation-User-rental_history")
    private User user;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security 시 발생 )
    @JsonBackReference(value = "relation-StoreInfo-rental_history")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rentalStore_Id")
    private StoreInfo rentalStore;

    @Builder
    public rental_history(LocalDateTime rentalAT, Cup cup, User user, StoreInfo store, Integer checkValue)
    {
        this.rentalAT = rentalAT;
        this.user = user;
        this.cup = cup;
        this.rentalStore = store;
        this.checkValue = checkValue;
    }
}

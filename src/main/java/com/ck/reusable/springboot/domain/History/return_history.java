package com.ck.reusable.springboot.domain.History;

import com.ck.reusable.springboot.domain.Store.StoreInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Table(name="return_history")
public class return_history {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long return_id;


    // insertable 써줘야지 insert 자동으로 안함!!
    @Column(nullable = false)
    private LocalDateTime returnAT;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security 시 발생 )
    @JsonBackReference(value = "relation-StoreInfo-return_history")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "returnStore_Id")
    private StoreInfo returnStore;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rentalHistory_Id")
    private rental_history rentalH;


    @Builder
    public return_history(LocalDateTime returnAT, StoreInfo returnStore, rental_history rentalH)
    {
        this.returnAT = returnAT;
        this.returnStore = returnStore;
        this.rentalH = rentalH;
    }
}

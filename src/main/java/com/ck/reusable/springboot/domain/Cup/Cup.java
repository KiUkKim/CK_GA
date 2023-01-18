package com.ck.reusable.springboot.domain.Cup;

import com.ck.reusable.springboot.domain.BaseTimeEntity;
import com.ck.reusable.springboot.domain.History.rental_history;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name="Cup")
public class Cup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodAttitudeCup_Uid;

    /*
    cup_state -- 0 대여가능, 1 대여중 , 2 반납, 3 세척
     */
    @Column(nullable = false)
    private Integer cupState;

    // 컵 사용 횟수
    @Column
    private Integer CupTotalCnt;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security시 발생 )
    @JsonManagedReference(value = "relation-Cup-rental_history")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cup", orphanRemoval = true)
    private List<rental_history> rental_histories = new ArrayList<>();

    @Builder
    public Cup(Integer cupState, List<rental_history> rental_histories)
    {
        this.cupState = cupState;
    }

}

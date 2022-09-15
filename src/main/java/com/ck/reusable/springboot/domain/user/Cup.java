package com.ck.reusable.springboot.domain.user;

import com.ck.reusable.springboot.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
@Table(name="Cup")
public class Cup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cupUid;

    /*
    cup_state -- 0 대여가능, 1 대여중 , 2 반납, 3 세척
     */
    @Column(nullable = false)
    private Integer cupState;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cup", orphanRemoval = true)
    private List<rental_history> rental_histories = new ArrayList<>();


    @Builder
    public Cup(Integer cupState, List<rental_history> rental_histories)
    {
        this.cupState = cupState;
        this.rental_histories = rental_histories;
    }
}

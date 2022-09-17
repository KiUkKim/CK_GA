package com.ck.reusable.springboot.domain.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.Store;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
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

    @Column
    private LocalDateTime returnAT;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security시 발생 )
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonBackReference(value = "relation-User-rental_history")
    private User user;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security시 발생 )
    @JsonBackReference(value = "relation-StoreInfo-rental_history")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId")
    private StoreInfo store;

    @Builder
    public rental_history(LocalDateTime rentalAT, LocalDateTime returnAT, Cup cup, User user, StoreInfo store)
    {
        this.rentalAT = rentalAT;
        this.returnAT = returnAT;
        this.user = user;
    }
}

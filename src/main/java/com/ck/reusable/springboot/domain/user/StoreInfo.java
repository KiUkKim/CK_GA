package com.ck.reusable.springboot.domain.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name="Store")
public class StoreInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String image_url;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String business_hours;

    @Column(columnDefinition = "TEXT")
    private String tag;

    //fastjson 순환 의존
    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    // value 값으로 관계 나타내주기!! (security시 발생 )
    @JsonManagedReference(value = "relation-StoreInfo-rental_history")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<rental_history> rental_histories = new ArrayList<>();

    @JsonManagedReference(value = "relation-StoreInfo-User")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<User> userStoreManager = new ArrayList<>();


    @Builder
    public StoreInfo(Double latitude, Double longitude, String image_url, String title, String business_hours, String tag, List<rental_history> rental_histories)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.image_url = image_url;
        this.title = title;
        this.business_hours = business_hours;
        this.tag = tag;
    }

}

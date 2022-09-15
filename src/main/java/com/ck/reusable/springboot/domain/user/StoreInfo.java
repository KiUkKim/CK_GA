package com.ck.reusable.springboot.domain.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
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

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store", orphanRemoval = true)
    private List<rental_history> rental_histories = new ArrayList<>();


    @Builder
    public StoreInfo(Double latitude, Double longitude, String image_url, String title, String business_hours, String tag, List<rental_history> rental_histories)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.image_url = image_url;
        this.title = title;
        this.business_hours = business_hours;
        this.tag = tag;
        this.rental_histories = rental_histories;
    }

}

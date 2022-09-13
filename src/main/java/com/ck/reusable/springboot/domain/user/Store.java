//package com.ck.reusable.springboot.domain.user;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import lombok.*;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Entity
//@Builder
//@Data
//@Table(name="Store")
//public class Store {
//
//    /*
//    매장 관련 정보들 User -> Store [N : 1]
//     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long Store_PK;
//
//    @Column(name = "x", columnDefinition = "TEXT")
//    private String x;
//
//    @Column(name = "y", columnDefinition = "TEXT")
//    private String y;
//
//    @Column(name = "tel", columnDefinition = "TEXT")
//    private String tel;
//
//    @Column(name = "name", columnDefinition = "TEXT")
//    private String name;
//
//    @JsonManagedReference
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Store", orphanRemoval = true)
//    private List<User> userList = new ArrayList<>();
//}

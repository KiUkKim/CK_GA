package com.ck.reusable.springboot.domain.user;

import com.ck.reusable.springboot.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Table(name="Member")
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_seq;

    /*
    {
        "name" : "최지문",
        "passwd" : "12121",
        "email" : "z@naver.com",
        "tel" : "010-0000-0000"
    }
     */

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String tel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(nullable = false)
    private String roles;

    @Column(name = "now_cnt", columnDefinition = "TEXT")
    private Integer now_cnt;

    @Column(name = "total_cnt", columnDefinition = "TEXT")
    private Integer total_cnt;

    //com.fasterxml.jackson.databind.JsonMappingException: Multiple back-reference properties with name 'defaultReference' 해결하기
    // 직렬화가 중복으로 일어남
    @JsonManagedReference(value = "relation-User-rental_history")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<rental_history> rental_histories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "relation-StoreInfo-User")
    @JoinColumn(name = "storeId")
    private StoreInfo storeInfo;

    @Builder
    public User(String name, String email, String tel, String password, String roles, Integer now_cnt, Integer total_cnt, List<rental_history> rental_histories)
    {
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.password = password;
        this.roles = roles;
        this.now_cnt = now_cnt;
        this.total_cnt = total_cnt;
        this.rental_histories = rental_histories;
    }

    public void addRole(String role)
    {
        this.roles = role;
    }
}

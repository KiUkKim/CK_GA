package com.ck.reusable.springboot.domain.user;

import com.ck.reusable.springboot.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mysql.cj.protocol.x.Notice;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
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

    @Builder
    public User(String name, String email, String tel, String password, String roles)
    {
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.password = password;
        this.roles = roles;
    }

    public void addRole(String role)
    {
        this.roles = role;
    }

}

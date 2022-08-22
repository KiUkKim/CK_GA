package com.ck.reusable.springboot.domain.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="jwtrefreshtoken")
public class RefreshJwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String tokenName;

    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name="created")
    @CreationTimestamp
    private Timestamp created;

}

package com.ck.reusable.springboot.domain.user;

import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Table(name="UserCard")
public class UserCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardInfo_seq;

    @Column(nullable = false)
    private String merchant_uid;

    @Column(nullable = false)
    private String customer_uid;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserCard(String merchant_uid, String customer_uid, User user)
    {
        this.merchant_uid = merchant_uid;
        this.customer_uid = customer_uid;
        this.user = user;
    }
}

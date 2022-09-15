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
@Builder
@Data
@Table(name="rental_history")
public class rental_history {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rental_id;

    @Column
    private LocalDateTime rentalAT;

    @Column
    private LocalDateTime returnAT;

    @JsonBackReference
    @ManyToOne(targetEntity = Cup.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "cupUid")
    private Cup cup;

    @JsonBackReference
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @JsonBackReference
    @ManyToOne(targetEntity = StoreInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId")
    private StoreInfo store;

    @Builder
    public rental_history(LocalDateTime rentalAT, LocalDateTime returnAT, Cup cup, User user, StoreInfo store)
    {
        this.rentalAT = rentalAT;
        this.returnAT = returnAT;
        this.cup = cup;
        this.user = user;
        this.store = store;
    }
}

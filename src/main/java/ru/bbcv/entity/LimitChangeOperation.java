package ru.bbcv.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "limit_change_operation")
public class LimitChangeOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LimitChangeStatus status;

    @Column(name = "username")
    private String username;

    @Column(name = "reservation_amount")
    private BigDecimal reservationAmount;

}

package com.doobie.trading.model;

import com.doobie.trading.domain.OrderStatus;
import com.doobie.trading.domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private OrderType orderType;

    private LocalDateTime timeStamp=LocalDateTime.now();

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderItem orderItem;
}

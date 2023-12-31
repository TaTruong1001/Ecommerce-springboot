package com.example.doan_tatruong.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_info")
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_info_id")
    private long id;
    @ManyToOne
    private User user;
    private Date paymentDate;
    private String paymentContent;
    private String cartInformation;
}

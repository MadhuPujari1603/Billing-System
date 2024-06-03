package com.project.billing.Dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double totalAmount;

    private String orbitGstNo;

    @CreationTimestamp
    private LocalDate localDate;

    @CreationTimestamp
    private LocalTime localTime;

    private String paymentMode;

    private String custName;

    private String custGST;

    private String custPhone;

    private String custEmail;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    @JsonManagedReference("Bill")
    private List<BillProduct> billProduct;

}
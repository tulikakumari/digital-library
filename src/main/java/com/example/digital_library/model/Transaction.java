package com.example.digital_library.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated
    private TransactionType txnType;

    @CreationTimestamp
    private Timestamp createdOn;

    @UpdateTimestamp
    private Timestamp updatedOn;

    @JoinColumn
    @ManyToOne
    private Book book;

    @JoinColumn
    @ManyToOne
    private Student student;

    @Enumerated(value = EnumType.STRING)
    private TransactionStatus txnStatus;

    private Long fine;
}

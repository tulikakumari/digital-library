package com.example.digital_library.dtos;

import com.example.digital_library.model.Transaction;
import com.example.digital_library.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTxnRequest {

    @Positive
    private Integer bookId;

    @Positive
    private Integer studentId;

    @NotNull
    private TransactionType txnType;

//    public Transaction to()
//    {
//        Transaction.builder()
//                .bookId(this.bookId)
//                .studentId(this.studentId)
//                .txnType(this.txnType)
//                .build();
//    }
}

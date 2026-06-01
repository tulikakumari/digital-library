package com.example.digital_library.controller;

import com.example.digital_library.dtos.CreateTxnRequest;
import com.example.digital_library.model.Transaction;
import com.example.digital_library.services.TransactionService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public Integer initiateTxn(@Valid @RequestBody CreateTxnRequest createTxnRequest) throws BadRequestException {
        return this.transactionService.initiateTxn(createTxnRequest).getId();
    }
}

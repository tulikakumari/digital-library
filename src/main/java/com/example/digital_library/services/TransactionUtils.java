package com.example.digital_library.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionUtils {

    @Value("${book.max-allowed}")
    @Getter
    Integer maxBooksAllowedForIssuance;

}

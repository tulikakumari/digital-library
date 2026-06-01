package com.example.digital_library.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class TransactionUtils {

    @Value("${book.max-allowed}")
    @Getter
    Integer maxBooksAllowedForIssuance;

}

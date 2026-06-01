package com.example.digital_library.repositories;

import com.example.digital_library.model.Transaction;
import com.example.digital_library.model.TransactionStatus;
import com.example.digital_library.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.config.TxNamespaceHandler;

@Repository
public interface TransactionReposiotory extends JpaRepository <Transaction,Integer> {
   Transaction findTopByStudentIdAndBookIdAndTxnTypeAndTxnStatusOrderByIdDesc(Integer studentId,
                                                                              Integer bookId,
                                                                              TransactionType txnType,
                                                                              TransactionStatus txnStatus);

}

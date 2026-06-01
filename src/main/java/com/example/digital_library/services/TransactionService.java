package com.example.digital_library.services;

import com.example.digital_library.dtos.CreateTxnRequest;
import com.example.digital_library.dtos.Pair;
import com.example.digital_library.model.*;
import com.example.digital_library.repositories.TransactionReposiotory;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

//    @Value("${book.max-allowed}")
//    Integer maxBooksAllowedForIssuance;

    @Value("${book.return.due-date}")
    Integer maxDueDateAllowed;

    @Value("${book.fine-per-day}")
    Integer finePerDay;

    @Autowired
    TransactionUtils transactionUtils;

    @Autowired
    BookService bookService;

    @Autowired
    StudentService studentService;

    @Autowired
    TransactionReposiotory txnRepository;



    public Transaction initiateTxn(@Valid CreateTxnRequest createTxnRequest) throws BadRequestException{
        Pair studentbookPair = getBookStudentPair(createTxnRequest.getBookId(),createTxnRequest.getStudentId());

        if(createTxnRequest.getTxnType() == TransactionType.ISSUE)
        {
            return initiateIssueTxn(createTxnRequest,studentbookPair);
        }
        else {
            return initiateReturnTxn(createTxnRequest,studentbookPair);
        }
    }

    private Pair getBookStudentPair(Integer bookId, Integer studentId) throws BadRequestException {
        // get student and book from txn request
        Student student = this.studentService.getStudentDetails(studentId);
        Book book = this.bookService.getBookDetailsV2(bookId);

        if(book == null || student == null)
        {
            throw new BadRequestException("either bookId or studentId is missing");
        }

        return Pair.builder()
              .book(book)
              .student(student)
              .build();

    }

    private Transaction initiateIssueTxn(CreateTxnRequest createTxnRequest,Pair studentbookPair) throws BadRequestException{


        if(studentbookPair.getBook().getStudent()!=null)
        {
            throw new BadRequestException("Book is already assigned to student");
        }

        List<Book> issuedBooks = this.bookService.getBooksIssuedToStudent(createTxnRequest.getStudentId());

        if(issuedBooks !=null && issuedBooks.size() >= this.transactionUtils.getMaxBooksAllowedForIssuance())
        {
            throw new BadRequestException("Student already has reached books issuance limit");
        }
        // Creating the txn and marking the book unavailable
        Transaction transaction = getTxnObjectInPendingState(createTxnRequest);
        transaction.setFine(0l);
        transaction = this.txnRepository.save(transaction); // saved the transaction in the repo

        // now make the book unavailable -- means update the studentId in the book table
       try {
           this.bookService.issueBookToStudent(createTxnRequest.getBookId(), studentbookPair.getStudent());

           // now mark the status completed

           transaction.setTxnStatus(TransactionStatus.COMPLETED);
           this.txnRepository.save(transaction);
       }catch(Exception e)
       {
           //if any of the above operation in try blocks fails
           // we need to mark txn status as failed
           //mark the book available again
           //remove student id from book and make it null
           e.printStackTrace();
           transaction.setTxnStatus(TransactionStatus.FAILURE);
           this.txnRepository.save(transaction);
           studentbookPair.getBook().setStudent(null);
           this.bookService.createBook(studentbookPair.getBook());
       }
       return transaction;
    }

    private Transaction initiateReturnTxn(CreateTxnRequest createTxnRequest,Pair studentbookPair)throws BadRequestException{
        // get student and book from txn request
//        Student student = this.studentService.getStudentDetails(createTxnRequest.getStudentId());
//        Book book = this.bookService.getBookDetailsV2(createTxnRequest.getBookId());

        // validation
//        if(book == null || student == null)
//        {
//            throw new BadRequestException("either bookId or studentId is missing");
//        }
        if(studentbookPair.getBook().getStudent()==null || (studentbookPair.getBook().getStudent().getId()!=null && !studentbookPair.getBook().getStudent().getId().equals(createTxnRequest.getStudentId())))
        {
            throw new BadRequestException("Book is not assigned to this student");
        }

        // Creating the txn and marking the book available
        Transaction transaction = getTxnObjectInPendingState(createTxnRequest);
        this.txnRepository.save(transaction);

        try {
            // marking the book avialable means removing student details from the book table for the particular book
            //book.setStudent(null);
            this.bookService.issueBookToStudent(studentbookPair.getBook().getId(), null);

            // fine calculation
            Transaction issueTxn = this.txnRepository.findTopByStudentIdAndBookIdAndTxnTypeAndTxnStatusOrderByIdDesc(createTxnRequest.getStudentId(), createTxnRequest.getBookId(),
                    TransactionType.ISSUE, TransactionStatus.COMPLETED);

            transaction.setFine(calculateFine(issueTxn.getCreatedOn()));

            transaction.setTxnStatus(TransactionStatus.COMPLETED);
            this.txnRepository.save(transaction);
        }catch(Exception e)
        {
            e.printStackTrace();
            transaction.setTxnStatus(TransactionStatus.FAILURE);
            transaction.setFine(null);
            this.txnRepository.save(transaction);

            studentbookPair.getBook().setStudent(studentbookPair.getStudent());
            this.bookService.createBook(studentbookPair.getBook());
        }

        return transaction;

    }


    private Transaction getTxnObjectInPendingState(CreateTxnRequest createTxnRequest){

        return Transaction.builder()
                .book(
                        Book.builder().id(createTxnRequest.getBookId()).build()
                )
                .student(
                        Student.builder().id(createTxnRequest.getStudentId()).build()
                )
                .txnType(createTxnRequest.getTxnType())
                .txnStatus(TransactionStatus.PENDING)
                .build();
    }

    private long calculateFine(Date issueDate){

        long issueTimeInEpoch = issueDate.getTime();
        long currentTimeInEpoch = System.currentTimeMillis();

        long timeDiff = currentTimeInEpoch - issueTimeInEpoch;

        long daysPassed = TimeUnit.MILLISECONDS.toDays(timeDiff);

        if (daysPassed > maxDueDateAllowed) {
            return (daysPassed - maxDueDateAllowed) * finePerDay;
        }

        return 0;
    }
}

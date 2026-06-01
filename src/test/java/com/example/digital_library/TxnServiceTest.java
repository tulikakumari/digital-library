package com.example.digital_library;

import com.example.digital_library.dtos.CreateTxnRequest;
import com.example.digital_library.model.*;
import com.example.digital_library.repositories.StudentRepository;
import com.example.digital_library.repositories.TransactionReposiotory;
import com.example.digital_library.services.BookService;
import com.example.digital_library.services.StudentService;
import com.example.digital_library.services.TransactionService;
import com.example.digital_library.services.TransactionUtils;
import org.apache.coyote.BadRequestException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TxnServiceTest {

    @InjectMocks // similar to creating a new object a real object
    TransactionService transactionService;

    @Mock // an object which connects itself with the @injectmocks object
    StudentService studentService;

    @Mock
    BookService bookService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    TransactionUtils transactionUtils;

    @Mock
    TransactionReposiotory transactionRepository;

    @Test
    public void initiateIssueTxn()throws BadRequestException{
        Student student = Student.builder()
                .id(1)
                .name("Piyush")
                .department(Department.CIVIL)
                .build();

        Book book = Book.builder()
                .id(1)
                .bookName("Intro to Java")
                .genre(Genre.MATHEMATICS)
                .build();

        Transaction transaction = Transaction.builder()
                .id(10)
                .txnStatus(TransactionStatus.COMPLETED)
                .student(student)
                .book(book)
                .txnType(TransactionType.ISSUE)
                .build();

        Mockito.when(studentService.getStudentDetails(1)).thenReturn(student);
        Mockito.when(bookService.getBookDetailsV2(1)).thenReturn(book);

        Mockito.when(transactionUtils.getMaxBooksAllowedForIssuance()).thenReturn(3);
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        Transaction actualTxn = transactionService.initiateTxn(CreateTxnRequest.builder()
                .txnType(TransactionType.ISSUE)
                .bookId(1)
                .studentId(1)
                .build()
        );

        Assert.assertEquals(transaction.getId(), actualTxn.getId());
        Assert.assertEquals(TransactionStatus.COMPLETED, actualTxn.getTxnStatus());


        Mockito.verify(transactionRepository, Mockito.times(2)).save(Mockito.any());
        Mockito.verify(bookService, Mockito.times(1)).issueBookToStudent(Mockito.anyInt(), Mockito.any());



    }

    @Test(expected = BadRequestException.class)
    public void initiateIssueTxn_StudentNotFound() throws BadRequestException {

        Mockito.when(studentService.getStudentDetails(1)).thenReturn(null);

        this.transactionService.initiateTxn(CreateTxnRequest.builder()
                .txnType(TransactionType.ISSUE)
                .bookId(1)
                .studentId(1)
                .build());
    }

    @Test(expected = BadRequestException.class)
    public void initiateIssueTxn_BookNotFound() throws BadRequestException {
        Student student = Student.builder()
                .id(1)
                .name("Piyush")
                .department(Department.CIVIL)
                .build();

//        Book book = Book.builder()
//                .id(1)
//                .name("Intro to Java")
//                .genre(Genre.MATHEMATICS)
//                .build();

        Mockito.when(studentService.getStudentDetails(1)).thenReturn(student);
        Mockito.when(bookService.getBookDetailsV2(1)).thenReturn(null);
//        Mockito.when(transactionUtils.getMaxAllowedBooksForIssuance()).thenReturn(3);


        this.transactionService.initiateTxn(CreateTxnRequest.builder()
                .txnType(TransactionType.ISSUE)
                .bookId(1)
                .studentId(1)
                .build());
    }

    @Test(expected = BadRequestException.class)
    public void initiateIssueTxn_StudentLimitReached() throws BadRequestException {

        Student student = Student.builder()
                .id(1)
                .name("Piyush")
                .department(Department.CIVIL)
                .build();

        Book book = Book.builder()
                .id(1)
                .bookName("Intro to Java")
                .genre(Genre.MATHEMATICS)
                .build();

        List<Book> booksIssued = Arrays.asList(
                Book.builder().id(4).build(),
                Book.builder().id(5).build(),
                Book.builder().id(6).build()
        );


        Mockito.when(studentService.getStudentDetails(1)).thenReturn(student);
        Mockito.when(bookService.getBookDetailsV2(1)).thenReturn(book);
        Mockito.when(transactionUtils.getMaxBooksAllowedForIssuance()).thenReturn(3);
        Mockito.when(bookService.getBooksIssuedToStudent(1)).thenReturn(booksIssued);

        this.transactionService.initiateTxn(CreateTxnRequest.builder()
                .txnType(TransactionType.ISSUE)
                .bookId(1)
                .studentId(1)
                .build());
    }

    @Test(expected = BadRequestException.class)
    public void initiateIssueTxn_BookAlreadyAssigned() throws BadRequestException{

        Student student = Student.builder()
                .id(1)
                .name("Piyush")
                .department(Department.CIVIL)
                .build();

        Book book = Book.builder()
                .id(1)
                .bookName("Intro to Java")
                .genre(Genre.MATHEMATICS)
                .student(student)
                .build();

        Mockito.when(studentService.getStudentDetails(1)).thenReturn(student);
        Mockito.when(bookService.getBookDetailsV2(1)).thenReturn(book);

        this.transactionService.initiateTxn(CreateTxnRequest.builder()
                .txnType(TransactionType.ISSUE)
                .bookId(1)
                .studentId(1)
                .build());
    }




}

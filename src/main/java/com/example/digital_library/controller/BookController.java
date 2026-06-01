package com.example.digital_library.controller;

import com.example.digital_library.dtos.BookResponse;
import com.example.digital_library.dtos.CreateBookRequest;
import com.example.digital_library.model.Book;
import com.example.digital_library.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/book")
    public Integer createBook(@Valid @RequestBody CreateBookRequest createBookRequest)
    {
        return this.bookService.createBook(createBookRequest);
    }

    @GetMapping("/book/{bookId}")
    public BookResponse getBookDetails(@PathVariable("bookId") Integer id){
        return this.bookService.getBookDetails(id);
    }

}

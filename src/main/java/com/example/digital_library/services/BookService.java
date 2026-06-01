package com.example.digital_library.services;

import com.example.digital_library.dtos.BookResponse;
import com.example.digital_library.dtos.CreateBookRequest;
import com.example.digital_library.model.Author;
import com.example.digital_library.model.Book;
import com.example.digital_library.model.Student;
import com.example.digital_library.repositories.AuthorRepository;
import com.example.digital_library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorService authorService;

    public Integer createBook(CreateBookRequest createBookRequest){
        Book book = createBookRequest.to();
        Author author = book.getAuthor();

        author = this.authorService.getOrCreateAuthor(author);

        book.setAuthor(author);
        this.bookRepository.save(book);
        return book.getId();
    }

    public void createBook(Book book)
    {
        this.bookRepository.save(book);
    }
    public BookResponse getBookDetails(Integer id)
    {
      Book book =  this.bookRepository.findById(id).orElse(null);
        return book == null ? null : BookResponse.from(book);
    }

    public Book getBookDetailsV2(Integer id)
    {
        return  this.bookRepository.findById(id).orElse(null);

    }

    public List<Book> getBooksIssuedToStudent(Integer studentId){
        return this.bookRepository.findByStudentId(studentId);
    }

    public void issueBookToStudent(Integer bookID, Student student){
        this.bookRepository.updateBookAvailability(bookID,student);
    }

}

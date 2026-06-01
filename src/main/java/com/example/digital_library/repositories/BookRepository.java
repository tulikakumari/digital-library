package com.example.digital_library.repositories;

import com.example.digital_library.model.Book;
import com.example.digital_library.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    List<Book> findByStudentId(Integer studentId);
    @Transactional
    @Modifying
    @Query("update Book b set b.student = ?2 where b.id = ?1")
    void updateBookAvailability(Integer bookId, Student student);
}

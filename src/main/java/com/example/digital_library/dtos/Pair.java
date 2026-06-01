package com.example.digital_library.dtos;

import com.example.digital_library.model.Book;
import com.example.digital_library.model.Student;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Pair {
    private Student student;
    private Book book;
}

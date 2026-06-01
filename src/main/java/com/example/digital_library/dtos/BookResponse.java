package com.example.digital_library.dtos;

import com.example.digital_library.model.Author;
import com.example.digital_library.model.Book;
import com.example.digital_library.model.Genre;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {


    private String name;
    private Genre genre;
    private Author author;

    public static BookResponse from(Book book){
        return BookResponse.builder()
                .name(book.getBookName())
                        .author(book.getAuthor())
                        .genre(book.getGenre())
                .build();
    }


}

package com.example.digital_library.dtos;

import com.example.digital_library.model.Author;
import com.example.digital_library.model.Book;
import com.example.digital_library.model.Genre;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateBookRequest {

    @NotBlank
    private String bookName;
    private Genre genre;

    //Author's Details

    @NotBlank
    private String authorName;
    @NotBlank
    @Email
    private String authorEmail;
    private String authorCountry;

    public Book to() {
        return Book.builder()
                .bookName(this.bookName)
                .genre(this.genre)
                .author(
                        Author.builder()
                                .name(this.authorName)
                                .email(this.authorEmail)
                                .country(this.authorCountry)
                                .build()
                ).build();
    }

}

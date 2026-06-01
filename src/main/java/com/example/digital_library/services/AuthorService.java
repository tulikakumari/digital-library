package com.example.digital_library.services;


import com.example.digital_library.model.Author;
import com.example.digital_library.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;
    public Author getOrCreateAuthor(Author author){
        Author authorFromDb = this.authorRepository.findByEmail(author.getEmail());
        if(authorFromDb == null)
        {
            authorFromDb = createAuthor(author);
        }
        return authorFromDb;
    }

    public Author createAuthor(Author author)
    {
        return this.authorRepository.save(author);
    }
}

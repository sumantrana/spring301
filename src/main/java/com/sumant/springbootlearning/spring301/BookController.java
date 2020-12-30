package com.sumant.springbootlearning.spring301;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class BookController {

    private static List<Book> bookRepository = new ArrayList<Book>();

    public BookController(){

        Book book = Book.builder()
                .id(1)
                .name("Spring Boot")
                .author("Josh Long")
                .price(40.5d)
                .build();
        bookRepository.add(book);

    }

    @GetMapping ("/books")
    public List<Book> getBooks(){

        return bookRepository;

    }

    @PostMapping ("/books")
    public Book createBook(@RequestBody Book newBook){

        newBook.setId(bookRepository.size() + 1 );
        bookRepository.add(newBook);
        return newBook;

    }
}

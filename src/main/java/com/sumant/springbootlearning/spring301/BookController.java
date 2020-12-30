package com.sumant.springbootlearning.spring301;


import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class BookController {

    private static Map<Integer, Book> bookRepository = new HashMap<Integer,Book>();

    public BookController(){
    }

    @GetMapping ("/books")
    public List<Book> getBooks(){

        return new ArrayList<>(bookRepository.values());

    }

    @PostMapping ("/books")
    public Book createBook(@RequestBody Book newBook){

        Integer id = bookRepository.size() + 1;
        newBook.setId( id );
        bookRepository.put(id, newBook);
        return newBook;

    }

    @PutMapping("/books/{id}")
    public Book createBook(@PathVariable Integer id, @RequestBody Book newBook){

        Book book = bookRepository.get(id);

        book.setName( newBook.getName() );
        book.setAuthor( newBook.getAuthor() );
        book.setPrice( newBook.getPrice() );

        bookRepository.put( id, book );

        return book;

    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Integer id){
        bookRepository.remove(id);
    }

}

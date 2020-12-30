package com.sumant.springbootlearning.spring301;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @GetMapping ("/books")
    public void getBooks(){

    }
}

package com.sumant.springbootlearning.spring301;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    /**
     * Now that we have all the CRUD APIs ready we can use them to setup and teardown each test case.
     * This removes the dependency on ordering of Test Cases.
     */

    @BeforeEach
    public void setup() throws Exception {

        Book newBook = Book.builder()
                .id(1)
                .name("Spring Boot")
                .author("Josh Long")
                .price(40.5d)
                .build();

        String newBookJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform( post("/books").content(newBookJson).contentType(MediaType.APPLICATION_JSON) );

    }

    @AfterEach
    public void tearDown() throws Exception {
        deleteBook(1);
    }


    /**
     * This test can be disabled or deleted. Shows the importance of updating the test cases as the code is developed
     * over time
     */
    @Test
    @Disabled
    public void getBook_WillReturn_404() throws Exception {
        mockMvc.perform( get("/books") ).andExpect( status().is(404) );
    }

    @Test
    public void getBook_WillReturn_BookList() throws Exception {
        mockMvc.perform( get("/books") )
                .andExpect( status().is(200) )
                .andExpect( jsonPath("$[0].name", is("Spring Boot")))
                .andExpect( jsonPath("$[0].author", is("Josh Long")))
                .andExpect( jsonPath("$[0].price", is(40.5), Double.class));
    }

    @Test
    public void postBook_WillCreateBook() throws Exception {

        Book newBook = Book.builder()
                .name("New Book")
                .author("New Author")
                .price(10.0d)
                .build();

        String newBookJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform( post("/books").content(newBookJson).contentType(MediaType.APPLICATION_JSON) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.name", is("New Book")))
                .andExpect( jsonPath("$.author", is("New Author")))
                .andExpect( jsonPath("$.price", is(10.0), Double.class));

        //This is an incomplete test till this point because we are not sure if the newly created book was persisted or not
        //To verify call the get books and ascertain that

        mockMvc.perform( get("/books") )
                .andExpect( status().is(200) )
                .andExpect( jsonPath("$", hasSize(2)))
                .andExpect( jsonPath("$[1].name", is("New Book")))
                .andExpect( jsonPath("$[1].author", is("New Author")))
                .andExpect( jsonPath("$[1].price", is(10.0), Double.class));

        deleteBook(2);

    }

    @Test
    public void putBook_WillUpdateBook_WithGivenId() throws Exception {

        Book updatedBook = Book.builder()
                .name("Spring Boot")
                .author("Josh Long")
                .price(20.0d)
                .build();

        String newBookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform( put("/books/1").content(newBookJson).contentType(MediaType.APPLICATION_JSON) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.name", is("Spring Boot")))
                .andExpect( jsonPath("$.author", is("Josh Long")))
                .andExpect( jsonPath("$.price", is(20.0), Double.class));

        //This is an incomplete test till this point because we are not sure if the updated book was persisted or not
        //To verify call the get books and ascertain that

        mockMvc.perform( get("/books") )
                .andExpect( status().is(200) )
                .andExpect( jsonPath("$", hasSize(1)))
                .andExpect( jsonPath("$[0].price", is(20.0), Double.class));

    }

    @Test
    public void deleteBook_WillDeleteBook_WithGivenId() throws Exception {

        mockMvc.perform( delete("/books/1") )
                .andExpect( status().isOk() );

        mockMvc.perform( get("/books") )
                .andExpect( status().is(200) )
                .andExpect( jsonPath("$", hasSize(0)));

    }

    private void deleteBook(int id) throws Exception {
        String bookUrl = "/books/" + id;
        mockMvc.perform( delete(bookUrl) );
    }

}

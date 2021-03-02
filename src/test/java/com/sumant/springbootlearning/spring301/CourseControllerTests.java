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

@WebMvcTest(CourseController.class)
public class CourseControllerTests {

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

        Course newCourse = Course.builder()
                .id(1)
                .name("Spring Boot")
                .build();

        String newCourseJson = objectMapper.writeValueAsString(newCourse);

        mockMvc.perform( post("/Courses").content(newCourseJson).contentType(MediaType.APPLICATION_JSON) );

    }

    @AfterEach
    public void tearDown() throws Exception {
        deleteCourse(1);
    }


    /**
     * This test can be disabled or deleted. Shows the importance of updating the test cases as the code is developed
     * over time
     */
    @Test
    @Disabled
    public void getCourse_WillReturn_404() throws Exception {
        mockMvc.perform( get("/Courses") ).andExpect( status().is(404) );
    }

    @Test
    public void getCourse_WillReturn_CourseList() throws Exception {
        mockMvc.perform( get("/Courses") )
                .andExpect( status().is(200) )
                .andExpect( jsonPath("$[0].name", is("Spring Boot")));
    }

    @Test
    public void postCourse_WillCreateCourse() throws Exception {

        Course newCourse = Course.builder()
                .name("New Course")
                .build();

        String newCourseJson = objectMapper.writeValueAsString(newCourse);

        mockMvc.perform( post("/Courses").content(newCourseJson).contentType(MediaType.APPLICATION_JSON) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.name", is("New Course")));

        //This is an incomplete test till this point because we are not sure if the newly created Course was persisted or not
        //To verify call the get Courses and ascertain that

        mockMvc.perform( get("/Courses") )
                .andExpect( status().is(200) )
                .andExpect( jsonPath("$", hasSize(2)))
                .andExpect( jsonPath("$[1].name", is("New Course")));

        deleteCourse(2);

    }

    @Test
    public void putCourse_WillUpdateCourse_WithGivenId() throws Exception {

        Course updatedCourse = Course.builder()
                .name("Spring Boot New")
                .build();

        String newCourseJson = objectMapper.writeValueAsString(updatedCourse);

        mockMvc.perform( put("/Courses/1").content(newCourseJson).contentType(MediaType.APPLICATION_JSON) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.name", is("Spring Boot New")));

        //This is an incomplete test till this point because we are not sure if the updated Course was persisted or not
        //To verify call the get Courses and ascertain that

        mockMvc.perform( get("/Courses") )
                .andExpect( status().is(200) )
                .andExpect( jsonPath("$", hasSize(1)))
                .andExpect( jsonPath("$[0].name", is("Spring Boot New")));

    }

    @Test
    public void deleteCourse_WillDeleteCourse_WithGivenId() throws Exception {

        mockMvc.perform( delete("/Courses/1") )
                .andExpect( status().isOk() );

        mockMvc.perform( get("/Courses") )
                .andExpect( status().is(200) )
                .andExpect( jsonPath("$", hasSize(0)));

    }

    private void deleteCourse(int id) throws Exception {
        String CourseUrl = "/Courses/" + id;
        mockMvc.perform( delete(CourseUrl) );
    }

}

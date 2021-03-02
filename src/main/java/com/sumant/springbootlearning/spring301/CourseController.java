package com.sumant.springbootlearning.spring301;


import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CourseController {

    private static Map<Integer, Course> CourseRepository = new HashMap<Integer,Course>();

    public CourseController(){
    }

    @GetMapping ("/Courses")
    public List<Course> getCourses(){

        return new ArrayList<>(CourseRepository.values());

    }

    @PostMapping ("/Courses")
    public Course createCourse(@RequestBody Course newCourse){

        Integer id = CourseRepository.size() + 1;
        newCourse.setId( id );
        CourseRepository.put(id, newCourse);
        return newCourse;

    }

    @PutMapping("/Courses/{id}")
    public Course createCourse(@PathVariable Integer id, @RequestBody Course newCourse){

        Course Course = CourseRepository.get(id);

        Course.setName( newCourse.getName() );

        CourseRepository.put( id, Course );

        return Course;

    }

    @DeleteMapping("/Courses/{id}")
    public void deleteCourse(@PathVariable Integer id){
        CourseRepository.remove(id);
    }

}

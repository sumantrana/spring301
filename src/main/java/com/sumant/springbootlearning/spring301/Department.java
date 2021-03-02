package com.sumant.springbootlearning.spring301;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    private Integer id;
    private String name;
    private List<Course> courseList = new ArrayList<>();

}

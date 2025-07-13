package com.example.studentdb.model;

import jakarta.validation.constraints.*;

public class Student {

    @NotNull
    private Integer id;

    @NotBlank
    private String FirstName;

    @NotBlank
    private String LastName;

    @NotNull
    @Min(1)
    private Integer age;

    @NotBlank
    private String course;

    @NotNull
    private Integer marks;
    // Getters and Setters
    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }
    public Integer getMarks() {
        return marks;
    }
    public void setMarks(Integer marks) {
        this.marks = marks;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getLastName() {
        return LastName;
    }
    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
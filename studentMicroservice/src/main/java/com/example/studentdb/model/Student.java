package com.example.studentdb.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "student")
public class Student {
    @Column(name = "id")
    @Id
    @NotNull
    private Integer id;

    @Column(name = "first_name")  
    @NotBlank
    private String firstName;

    @Column(name = "last_name")  
    @NotBlank
    private String lastName;

    @Column(name = "age")
    @NotNull
    @Min(1)
    private Integer age;

    @Column(name = "course")
    @NotBlank
    private String course;

    @Column(name = "marks")
    @NotNull
    private Integer marks;
    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
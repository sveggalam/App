package com.example.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "library")
public class Library {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "course")
    private String course;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}

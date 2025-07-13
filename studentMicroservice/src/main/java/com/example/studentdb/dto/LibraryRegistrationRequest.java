package com.example.studentdb.dto;

public class LibraryRegistrationRequest {
    private Integer id;
    private String course;

    public LibraryRegistrationRequest() {}

    public LibraryRegistrationRequest(Integer id, String course) {
        this.id = id;
        this.course = course;
    }

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

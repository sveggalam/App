package com.example.library.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_books")
public class CourseBooks {

    @Id
    private String course;

    @ManyToMany
    private List<Book> books;

    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

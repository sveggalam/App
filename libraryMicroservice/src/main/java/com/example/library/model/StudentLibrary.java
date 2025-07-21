package com.example.library.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "student_library")
public class StudentLibrary {

    @Id
    private Integer studentId;

    private String course;

    @ManyToMany
    private List<Book> books;

    // Optional: Link to CourseBooks just for reference (read-only)
    @ManyToOne
    @JoinColumn(name = "course", referencedColumnName = "course", insertable = false, updatable = false)
    private CourseBooks courseBooks;

    // Getters and Setters
    public Integer getStudentId() {
        return studentId;
    }
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
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
    public CourseBooks getCourseBooks() {
        return courseBooks;
    }
    public void setCourseBooks(CourseBooks courseBooks) {
        this.courseBooks = courseBooks;
    }
}

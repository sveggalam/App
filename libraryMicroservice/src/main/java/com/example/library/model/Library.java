package com.example.library.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="library")
public class Library {
    @Column(name="id")
    @Id 
    private Integer id;
    @Column(name="course")
    private String course;
    public Integer getId(){
        return this.id;
    }
    public String getCourse(){
        return this.course;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public void setCourse(String subject){
        this.course = subject;
    }
}

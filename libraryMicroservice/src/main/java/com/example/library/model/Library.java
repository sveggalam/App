package com.example.library.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="library")
public class Library {
    @Id 
    private Integer id;
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

package com.example.library.model;

public class Library {
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

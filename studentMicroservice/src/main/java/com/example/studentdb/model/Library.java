package com.example.studentdb.model;

import java.util.List;

public class Library {
    private Integer id;
    private String subject;
    private List<String>booksAccess;
    public Integer getId(){
        return this.id;
    }
    public String getSubject(){
        return this.subject;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    public void setBooksAccess(List<String>subjects){
        this.booksAccess = subjects;
    }
    public List<String> getBooksAccess(){
        return this.booksAccess;
    }

}

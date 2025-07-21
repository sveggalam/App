package com.example.studentdb.repository;

import com.example.studentdb.model.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

public interface StudentRepository extends JpaRepository<Student,Integer>{
    List<Student> findByMarksBetween(Integer min, Integer max);
    // List<Student> findByFirstName(String firstName);
    List<Student> findByLastName(String lastName);
    List<Student> findByFirstNameContainingIgnoreCase(String firstName);
    List<Student> findByLastNameContainingIgnoreCase(String firstName);
    
    @Query("SELECT MAX(s.marks) FROM Student s")
    Integer findMaxMarks();

    List<Student> findByMarks(Integer marks);
}
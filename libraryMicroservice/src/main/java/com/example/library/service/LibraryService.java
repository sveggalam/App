package com.example.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.apache.tomcat.jni.Library;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.library.model.Library;
import com.example.library.repository.LibraryRepository;

import jakarta.validation.Valid;
import java.io.File;
import java.util.*;

@Service
public class LibraryService {
    @Autowired
    private LibraryRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(LibraryService.class);
    private static final String FILE_PATH = "library.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<Integer,String> idSubjectsMapper = new HashMap<>();
    private static final Map<String, List<String>> courseBooks = Map.ofEntries(
        Map.entry("Java", List.of("Effective Java", "Java Concurrency in Practice", "Spring in Action", "Head First Java", "Clean Code", "Java Performance")),
        Map.entry("Python", List.of("Fluent Python", "Automate the Boring Stuff with Python", "Python Crash Course", "Effective Python", "Learning Python", "Python Tricks")),
        Map.entry("C++", List.of("Effective Modern C++", "The C++ Programming Language", "Accelerated C++", "C++ Primer", "C++ Concurrency in Action", "C++ Templates")),
        Map.entry("JavaScript", List.of("You Don’t Know JS", "Eloquent JavaScript", "JavaScript: The Good Parts", "JavaScript: The Definitive Guide", "Learning JavaScript Design Patterns", "Secrets of the JavaScript Ninja")),
        Map.entry("Go", List.of("The Go Programming Language", "Go in Action", "Concurrency in Go", "Go Web Programming", "Mastering Go", "Go Design Patterns")),
        Map.entry("Rust", List.of("The Rust Programming Language", "Programming Rust", "Rust in Action", "Rust by Example", "Hands-On Concurrency with Rust", "Zero To Production In Rust")),
        Map.entry("C", List.of("The C Programming Language", "C in Depth", "Expert C Programming", "C Pocket Reference", "21st Century C", "C Programming: A Modern Approach")),
        Map.entry("Ruby", List.of("The Well-Grounded Rubyist", "Eloquent Ruby", "Practical Object-Oriented Design in Ruby", "Ruby on Rails Tutorial", "Programming Ruby", "Metaprogramming Ruby")),
        Map.entry("Kotlin", List.of("Kotlin in Action", "Atomic Kotlin", "Kotlin Programming: The Big Nerd Ranch Guide", "Programming Kotlin", "Kotlin for Android Developers", "Head First Kotlin")),
        Map.entry("Swift", List.of("Swift Programming: The Big Nerd Ranch Guide", "iOS Programming with Swift", "Swift for Beginners", "Hacking with Swift", "Advanced Swift", "Pro Swift")),
        Map.entry("PHP", List.of("PHP Objects, Patterns, and Practice", "Modern PHP", "PHP & MySQL: Novice to Ninja", "PHP Cookbook", "Laravel: Up and Running", "Programming PHP")),
        Map.entry("C#", List.of("C# in Depth", "Pro C# 10 with .NET 6", "CLR via C#", "Head First C#", "The C# Player's Guide", "C# 10 and .NET 6 – Modern Cross-Platform Development")),
        Map.entry("Scala", List.of("Programming in Scala", "Scala for the Impatient", "Functional Programming in Scala", "Scala Cookbook", "Scala Design Patterns", "Learning Scala")),
        Map.entry("Perl", List.of("Programming Perl", "Learning Perl", "Intermediate Perl", "Modern Perl", "Perl Best Practices", "Perl Cookbook")),
        Map.entry("TypeScript", List.of("Programming TypeScript", "TypeScript Quickly", "Effective TypeScript", "TypeScript in Plain Language", "Learning TypeScript", "TypeScript Design Patterns"))
    );


    public ResponseEntity<?> registerStudent(Library s) {
        // Check required fields manually
        if (s.getId() != null && repo.existsById(s.getId())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Student with ID " + s.getId() + " already exists.");
        }
        Library savedStudent = repo.save(s);
        return ResponseEntity.ok(savedStudent);
    }
    // public List<String> getListofBooks(Integer Id){
    //     List<Library>entries = readFromFile();
    //     logger.info("Requested List of Books availabale to ID {}",Id);
    //     if(entries.stream().anyMatch(s-> s.getId().equals(Id))){
    //         return courseBooks.get(idSubjectsMapper.get(Id));
    //     }
    //     return Collections.emptyList();
    // }
    // public boolean checkForBookAccess(Integer id,String bookName){
    //     if(!idSubjectsMapper.containsKey(id)){
    //         return false;
    //     }
    //     List<String>books = courseBooks.get(idSubjectsMapper.get(id));
    //     if(!books.contains(bookName))
    //         return false;
    //     return true;
    // }
    // public List<Library> getAllStudents() {
    //     logger.info("Requested List of all students registered to library");
    //     return readFromFile();
    // }
    // public List<String> getListofBooksByCourse(String courseName) {
    //     logger.info("Requested books available to people who has course{}",courseName);
    //     return courseBooks.getOrDefault(courseName, Collections.emptyList());
    // }
    
}
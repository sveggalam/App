package com.example.library.service;

import com.example.library.model.Library;
import com.example.library.repository.LibraryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LibraryService {

    private final LibraryRepository repo;

    public LibraryService(LibraryRepository repo) {
        this.repo = repo;
    }

    public Library registerStudent(Library s) {

        if (s.getId() != null && repo.existsById(s.getId())) {
            throw new RuntimeException(
                "Student with ID " + s.getId() + " already exists.");
        }

        return repo.save(s);
    }

    public List<Library> getAllStudents() {
        return repo.findAll();
    }
}

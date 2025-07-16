package com.example.studentdb.service;

import com.example.studentdb.model.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.studentdb.dto.LibraryRegistrationRequest;
import java.io.File;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.studentdb.Trie;
@Service

public class StudentService {
    private static final String FILE_PATH = "students.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<Integer,Student> studentMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private WebClient webClient;

    public StudentService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void notifyLibrary(Student student) {
        LibraryRegistrationRequest req = new LibraryRegistrationRequest();
        req.setId(student.getId());
        req.setCourse(student.getCourse());

        webClient.post()
            .uri("/library/register")
            .bodyValue(req)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(response -> {
                System.out.println("Library service response: " + response);
            });
    }

    private List<Student> readFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<Student>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    private void writeToFile(List<Student> students,Student student) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), students);
            notifyLibrary(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    public String insertStudent(Student student) {
        // Check required fields manually
        if (student.getId() == null ||
            isBlank(student.getFirstName()) ||
            isBlank(student.getLastName()) ||
            student.getAge() == null || student.getAge() < 18 ||
            isBlank(student.getCourse()) ||
            student.getMarks() == null) {
            logger.error("All fields not filled");
            return "Validation failed: all fields must be filled correctly.";
        }

        // Read from file
        List<Student> students = readFromFile();

        // Check for duplicate ID
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            return "Student ID already exists.";
        }

        // Add and save
        // trie.
        students.add(student);
        
        writeToFile(students,student);
        logger.info("Student Inserted Successfully");
        return "Student inserted successfully.";
    }

    public Map<String, Object> getAllStudents() {
        List<Student> students = readFromFile();
        Map<String, Object> result = new HashMap<>();
        result.put("count", students.size());
        result.put("students", students);
        logger.info("Requested for all Students");
        return result;
    }

   public Object getStudentById(Integer id) {
    List<Student> students = readFromFile();
    logger.info("Requested student {}",id);
    return students.stream()
            .filter(s -> s.getId().equals(id))
            .findFirst()
            .<Object>map(student -> student)
            .orElse("Student not found.");
    }
    public Map<String, Object> getStudentsInMarksRange(int minMarks, int maxMarks) {
        List<Student> students = readFromFile();

        if (students.isEmpty()) {
            return Collections.singletonMap("message", "No students found.");
        }

        List<Student> filtered = students.stream()
                .filter(s -> s.getMarks() >= minMarks && s.getMarks() <= maxMarks)
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("count", filtered.size());
        result.put("students", filtered);
        return result;
    }
    public List<Student> searchByFirstName(String firstName) {
        List<Student> students = readFromFile();
        logger.info("Requested Search by {}",firstName);
        return students.stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName))
                .toList(); // or .collect(Collectors.toList()) if using Java <16
    }
    public List<Student> searchTrie(String name, Trie trie) {
        List<Integer> ids = trie.search(name);
        List<Student> students = new ArrayList<>();
        for(Integer id : ids) {
            students.add(studentMap.get(id));
        }
        return students;
    }
    public Object getTopper() {
        List<Student> students = readFromFile();
        logger.info("Requested TOpper");
        if (students.isEmpty()) {
            return "No students found.";
        }

        return students.stream()
                .max(Comparator.comparingInt(Student::getMarks));
    }

    public Object getStandardDeviation() {
        List<Student> students = readFromFile();

        if (students.isEmpty()) {
            return "No students found.";
        }
        int maximum = students.stream()
                .mapToInt(Student::getMarks)
                .max()
                .orElse(0);
        int minimum = students.stream()
                .mapToInt(Student::getMarks)
                .min()
                .orElse(0);
        double mean = students.stream()
                .mapToInt(Student::getMarks)
                .average()
                .orElse(0.0);

        double variance = students.stream()
                .mapToDouble(s -> Math.pow(s.getMarks() - mean, 2))
                .average()
                .orElse(0.0);

        double stdDev = Math.sqrt(variance);
            return String.format(
        "Mean: %.2f, Variance: %.2f, Standard Deviation: %.2f, Maximum Marks: %d, Minimum Marks: %d",
        mean, variance, stdDev, maximum, minimum
    );
    }

}

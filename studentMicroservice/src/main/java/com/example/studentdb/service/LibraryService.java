package com.example.studentdb.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;
import com.example.studentdb.model.Library;
import com.example.studentdb.controller.libraryController;
// import com.example.studentdb.model.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class LibraryService {
    private static final String FILE_PATH = "library.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<Integer,String> subjectsMap = new HashMap<>();
    private Map<String,List<String>> subjectsBooks = new HashMap<>();
    LibraryService(){
        this.subjectsMap.put(1,"Maths");
        this.subjectsMap.put(2,"Physics");
        this.subjectsMap.put(3,"Social");
        this.subjectsMap.put(4,"Biology");
        this.subjectsMap.put(5,"Chemistry");
        this.subjectsMap.put(6,"English");
        for(int subjectNum=1;subjectNum<=6;subjectNum++){
            
        }
    }
    
    private List<Library> readFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<Library>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeToFile(List<Library> library) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), library);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String register(Library entry){
        if(entry.getId()==null)
            return "Validation Failed";

        List<Library>library = readFromFile();
        if(library.stream().anyMatch(s-> s.getId().equals(entry.getId())))
            return "Student already exists";
        // gen a random number
        Random r= new Random();
        int r1 = r.nextInt(7);
        entry.setSubject(subjectsMap.get(r1));
        library.add(entry);
        writeToFile(library);
        return "Student registered successfully";
    }

}



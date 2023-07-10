package com.secured.controller;

import com.secured.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/management/api/v1")
@Slf4j
public class StudentManagementController {

    @GetMapping("/students/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable("studentId") int id) {
        log.info("called -> {} - getStudentById", SOURCE_CLASS);
        id--;
        log.info("{} - getStudentById - parameter - {}", SOURCE_CLASS, id);
        return new ResponseEntity<>(this.STUDENTS.get(id), HttpStatus.OK);
    }

    @GetMapping("/students")
    public ResponseEntity<Student> getAllStudent() {
        log.info("called -> {} - getAllStudent", SOURCE_CLASS);
        log.info("getAllStudent - returned - {}", this.STUDENTS);
        return new ResponseEntity(this.STUDENTS, HttpStatus.OK);
    }

    @DeleteMapping("/students/{id}")
    public void deleteSingleStudent(@PathVariable("id") int id) {
        log.info("called -> {} - deleteSingleStudent", SOURCE_CLASS);
        id--;
        log.info("Successfully deleted the student with id: {}", id);
//        STUDENTS
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") int id, @RequestBody Student student) {
        log.info("called -> {} - updateStudent", SOURCE_CLASS);
        id--;
        student = STUDENTS.get(id);
//        STUDENTS.set(id, student);
        log.info("Successfully updated the student: {}", student);
        return new ResponseEntity<>(student, HttpStatus.ACCEPTED);
    }

    @PostMapping("/students")
    public ResponseEntity<Student> updateStudent( @RequestBody Student student) {
        log.info("called -> {} - updateStudent", SOURCE_CLASS);
        student.setId(STUDENTS.size());
        log.info("Successfully created new student: {}", student);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    private static List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Iron Man"),
            new Student(2, "Super Man"),
            new Student(3, "Bat Man"),
            new Student(4, "Gentle Man"));

    private static final String SOURCE_CLASS = "StudentManagementController";
}

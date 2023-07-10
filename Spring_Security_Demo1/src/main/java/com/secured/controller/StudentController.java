package com.secured.controller;

import java.util.List;
import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secured.model.Student;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class StudentController {
	
	@GetMapping("/students/{studentId}")
	public ResponseEntity<Student> getStudentById(@PathVariable("studentId") int id) {
		log.info("called - getStudentById");
		id--;
		log.info("getStudentById - parameter - {}", id);
		return new ResponseEntity<>(this.studentsList.get(id), HttpStatus.OK);
	}

	@GetMapping("/students")
	public ResponseEntity<Student> getAllStudent() {
		log.info("called - getAllStudent");
		log.info("getAllStudent - returned - {}", this.studentsList);
		return new ResponseEntity(this.studentsList, HttpStatus.OK);
	}
	
	public StudentController() {
		studentsList = new ArrayList<>();
		studentsList.add(new Student(1, "Iron Man"));
		studentsList.add(new Student(2, "Super Man"));
		studentsList.add(new Student(3, "Bat Man"));
		studentsList.add(new Student(4, "Gentle Man"));

	}
	

	List<Student> studentsList;
}

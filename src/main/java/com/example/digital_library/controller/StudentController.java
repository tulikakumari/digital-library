package com.example.digital_library.controller;

import com.example.digital_library.dtos.CreateStudentRequest;
import com.example.digital_library.model.Student;
import com.example.digital_library.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/students")
    public Integer createStudent(@Valid @RequestBody CreateStudentRequest createStudentRequest){

       return this.studentService.createStudent(createStudentRequest);

    }

    @GetMapping("/students/{studentId}")
    public Student getStudentDetails(@PathVariable("studentId") Integer id){
        return this.studentService.getStudentDetails(id);
    }
}

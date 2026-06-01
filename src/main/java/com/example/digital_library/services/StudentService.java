package com.example.digital_library.services;

import com.example.digital_library.dtos.CreateStudentRequest;
import com.example.digital_library.model.Student;
import com.example.digital_library.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public Integer createStudent(CreateStudentRequest createStudentRequest)
    {
        Student student = createStudentRequest.to();
        this.studentRepository.save(student);

        return student.getId();
    }

    public Student getStudentDetails(Integer id) {

        return this.studentRepository.findById(id).orElse(null);
    }
}

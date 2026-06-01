package com.example.digital_library.dtos;

import com.example.digital_library.model.Department;
import com.example.digital_library.model.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateStudentRequest {

    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String rollNo;

    private Department department;

    public Student to()
    {
        return Student.builder().
               rollNo(this.rollNo).
               email(this.email).
                name(this.name).
                department(this.department).build();

    }

}

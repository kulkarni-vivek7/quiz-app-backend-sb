package com.example.quiz_app.dto;

import com.example.quiz_app.enums.Subject;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentDTO {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @Min(10) @Max(100)
    private int age;

    @Email
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    @NotNull
    private Subject subject;
}

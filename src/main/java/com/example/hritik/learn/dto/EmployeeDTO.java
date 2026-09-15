package com.example.hritik.learn.dto;

import com.example.hritik.learn.annotations.EmployeeRoleValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

// POJO CLASS (Model class)
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;

    @NotBlank(message = "Name of emmployee cannot be blank")
    @Size(min=3, max=20, message = "min 3 max to chars allowed in name")
    private String name;

    @NotNull(message = "Age cannot be null")
    @Max(value = 80, message = "Age cannot be more than 80")
    @Min(value = 18, message = "Age cannot be less than 18")
    private Integer age;

    @NotBlank(message = "Role of the employee cannot be blank")
    //@Pattern(regexp = "^(ADMIN|USER)$", message = "Role can only be ADMIN or USER")
    // Alter of above is below
    @EmployeeRoleValidation
    private String role; //ADMIN or USER

    @NotBlank
    @Email(message = "Not a valid email")
    private String email;

    @PastOrPresent(message = "Date of joining cannot be from future")
    private LocalDate dateOfJoining;

    @NotNull @PositiveOrZero
    @Digits(integer = 6, fraction = 2, message = "salary can be in form XXXXXX.YY")
    @DecimalMin(value = "100000.00")
    @DecimalMax(value = "999999.99")
    private Double salary;

    @AssertTrue(message = "Employee status should be active")
    @JsonProperty("isActive") // because 'is' is causing some problem, and
    private Boolean isActive;

//    // All args constructor
//    public EmployeeDTO(Integer age, LocalDate dateOfJoining, String email, Long id, Boolean isActive, String name) {
//        this.id = id;
//        this.name = name;
//        this.age = age;
//        this.email = email;
//        this.dateOfJoining = dateOfJoining;
//        this.isActive = isActive;
//
//    }

//    // Default constructor
//    public EmployeeDTO() {
//    }


}

package com.example.hritik.learn.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity // hibernate takes below given table and converts it to database
@Getter @Setter // Else API will return {} (empty!)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employees")
public class EmployeeEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer age;

    private String role;

    private String email;
    private LocalDate dateOfJoining;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Double salary;
}

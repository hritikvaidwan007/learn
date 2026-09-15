package com.example.hritik.learn.repositories;

import com.example.hritik.learn.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

//    List<EmployeeEntity> findByName(String name); // no need of function definition, function is defined by JPA repository
    // Basic crud operations are already provided by JPA repository, no need to define them because we have extended JpaRepository

}

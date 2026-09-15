package com.example.hritik.learn.controllers;

import com.example.hritik.learn.dto.EmployeeDTO;
import com.example.hritik.learn.entities.EmployeeEntity;
import com.example.hritik.learn.repositories.EmployeeRepository;
import com.example.hritik.learn.services.EmployeeService;
import exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // constructor injection for spring-boot
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // In this controller, all function will use this exception handler when they throw 'NoSuchElementException'
    // Bad way: it will return 200
//    @ExceptionHandler(NoSuchElementException.class)
//    public String handleEmployeeNotFound(NoSuchElementException exception) {
//        return "Employee was not found";
//    }

    // In this controller, all function will use this exception handler when they throw 'NoSuchElementException'
    // Good way: it will return actual status code because we are using response entity
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<String> handleEmployeeNotFound(NoSuchElementException exception) {
//        // Method 1: using builder
//        //return ResponseEntity.notFound().build();
//
//        // Method 2: using constructor (check)
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
//    }


    // GET API to get employee using employee Id
    // ResponseEntity<EmployeeDTO> helps to return DTO + Status code, else we could have used EmployeeDTO instead.
    // now it returns 404 Not Found when employee does not exist
    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long employeeId) {
//        EmployeeDTO employeeDTO = employeeService.getById(employeeId);
//
//        if (employeeDTO == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(employeeDTO);

        // Optional<EmployeeDTO> means the method may return an EmployeeDTO, or it may return nothing
        // so it never returns null. so avoids NullPointerException. eg: employeeDTO.getName();
        Optional<EmployeeDTO> employeeDTO = employeeService.getById(employeeId);

        // if response entity found then "employeeDTO1 -> ResponseEntity.ok(employeeDTO1)"
        // alter: "return employeeDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());"
        return employeeDTO
                .map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1)) // ok means 200 status code
                //.orElse(ResponseEntity.notFound().build()); // 404 Not Found
                //.orElseThrow(() -> new NoSuchElementException("Employee not found"));
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));
    }

//    // GET API to get all employees (without response entity)
//    @GetMapping
//    public List<EmployeeDTO> getAllEmployees(@RequestParam(required = false) Integer age,
//                                  @RequestParam(required = false) String sortBy) {
//        return employeeService.getAllEmployees();
//    }

    // GET API to get all employees (With response entity)
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@RequestParam(required = false) Integer age,
                                             @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(employeeService.getAllEmployees()); // here orElse not added so that it can return empty list
    }

//    // POST API to post employee using Employee Request body (Without Response entity)
//    @PostMapping
//    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO inputEmployeeDTO) {
//       return employeeService.createNewEmployee(inputEmployeeDTO);
//    }

    // POST API to post employee using Employee Request body (With Response Entity)
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO inputEmployeeDTO) {
        EmployeeDTO savedEmployee = employeeService.createNewEmployee(inputEmployeeDTO);

        // Analogy: "Create an HTTP response whose body is savedEmployee and whose status is 201 Created."
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED); // this is constructor method 'ResponseEntity(T body, HttpStatusCode status)'
    }

//    // DELETE API (Without response entity)
//    @DeleteMapping(path = "/{employeeId}")
//    public boolean deleteEmployeeById(@PathVariable Long employeeId) {
//        return employeeService.deleteEmployeeById(employeeId);
//    }

    // DELETE API (With response entity)
    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable Long employeeId) {
        boolean gotDeleted = employeeService.deleteEmployeeById(employeeId);
        if (gotDeleted) {return ResponseEntity.ok(true);} // 200
        else {return ResponseEntity.notFound().build();} //404
    }

//    // PUT API is used when we update all the data for an existing Employee (Only Id will be same, all remaining fields will be updated)
//    @PutMapping(path = "/{employeeId}")
//    public EmployeeDTO updateEmployeeById(@RequestBody EmployeeDTO inputEmployeeDTO, @PathVariable Long employeeId) {
//        return employeeService.updateEmployeeId(employeeId, inputEmployeeDTO);
//    }

    // PUT API is used when we update all the data for an existing Employee (Only Id will be same, all remaining fields will be updated)
    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@RequestBody @Valid EmployeeDTO inputEmployeeDTO, @PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.updateEmployeeId(employeeId, inputEmployeeDTO));
    }

//    // PATCH API is used when we update data PARTIALLY for an existing Employee
//    @PatchMapping(path = "/{employeeId}")
//    public EmployeeDTO updatePartiallyEmployeeById(@RequestBody Map<String, Object> inputEmployeeUpdates, @PathVariable Long employeeId) {
//        return employeeService.updatePartiallyEmployeeById(employeeId, inputEmployeeUpdates);

    // PATCH API is used when we update data PARTIALLY for an existing Employee
    @PatchMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> updatePartiallyEmployeeById(@RequestBody Map<String, Object> inputEmployeeUpdates, @PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.updatePartiallyEmployeeById(employeeId, inputEmployeeUpdates);
        if(employeeDTO == null) return ResponseEntity.notFound().build(); // 404 // null return logic defined inside employee service
        return ResponseEntity.ok(employeeDTO); // 200
    }

}

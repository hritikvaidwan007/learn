package com.example.hritik.learn.services;

import com.example.hritik.learn.dto.EmployeeDTO;
import com.example.hritik.learn.entities.EmployeeEntity;
import com.example.hritik.learn.repositories.EmployeeRepository;
import exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    // for spring-boot injection
    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    // GET /employees/{employeeId}
    public Optional<EmployeeDTO> getById(Long id) {
//        // Get entity from database
//        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
//        // Convert Entity to DTO (using model mapper) and return
//        return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity1, EmployeeDTO.class));
//        //return modelMapper.map(employeeEntity, EmployeeDTO.class);

        // or (in 1 line)
        return employeeRepository.findById(id).map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class));
    }

    // GET /employees
    public List<EmployeeDTO> getAllEmployees() {
        // Get entity from database
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();

        // Convert Entity to DTO (using model mapper) and return
        return employeeEntities
                .stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    // POST /employees
    public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployeeDTO) {

        // convert Received dto to a temp entity
        EmployeeEntity toSaveEntity = modelMapper.map(inputEmployeeDTO, EmployeeEntity.class);

        // use .save() to save that entity, and save received data into 'savedEmployeeEntity'
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(toSaveEntity);

        // convert received saved 'savedEmployeeEntity' to DTO
        // question: cant we directly return 'inputEmployeeDTO' here. aren't both same?
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);
    }

    // DELETE /employees/{employeeId}
    public boolean deleteEmployeeById(Long employeeId) {
        boolean exists = employeeRepository.existsById(employeeId);
        if (!exists) {throw new ResourceNotFoundException("Could not delete because Employee not found with id: " + employeeId);}
        else {
            employeeRepository.deleteById(employeeId);
            return true;
        }
    }

    // PUT /employees/{employeeId}
    public EmployeeDTO updateEmployeeId(Long employeeId, EmployeeDTO inputEmployeeDTO) {

        // new 2 lines below related to exception handling, it can work without these 2 lines
        boolean exists = employeeRepository.existsById(employeeId);
        if (!exists) {throw new ResourceNotFoundException("Could not update because Employee not found with id: " + employeeId);}

        // Below logic is such that if already employee Id is not present, then it regiosters a new employee just like POST API
        // Basically we have created this as Upsert API instead of just update API
        EmployeeEntity employeeEntity = modelMapper.map(inputEmployeeDTO, EmployeeEntity.class);
        employeeEntity.setId(employeeId); // remember inputEmployeeDTO does not contain employee Id
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);

    }

    // PATCH /employees/{employeeId}
    public EmployeeDTO updatePartiallyEmployeeById(Long employeeId, Map<String, Object> inputEmployeeUpdates) {
        boolean exists = employeeRepository.existsById(employeeId);

        if (!exists) {throw new ResourceNotFoundException("Employee not found with id: " + employeeId);}
        else {
            // First fetch that employee  needs to be pathed (whose multiple fields needs to be updated)
            EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).orElse(null);

            //Using reflection: To directly goto any object and update fields of that object directly
            // for each field passed From the PATCH API
            inputEmployeeUpdates.forEach((field, value) -> {
                // First find field inside EmployeeEntity.class which needs to be updated and make it accessible
                Field fieldToBeUpdated = ReflectionUtils.findField(EmployeeEntity.class, field);
                fieldToBeUpdated.setAccessible(true); // Because EmployeeEntity fields are private (not accessible)

                // Now update the field to be updated (Field to be updated, where to be updated, with what field)
                ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value); // 3 params always (understood)
            });

            return modelMapper.map(employeeEntity, EmployeeDTO.class);
        }
    }
}

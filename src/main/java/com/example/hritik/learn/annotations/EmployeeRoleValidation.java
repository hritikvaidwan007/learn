package com.example.hritik.learn.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {EmployeeRoleValidator.class})
@Retention(RetentionPolicy.RUNTIME) // remember 3 types of RetentionPolicy
@Target(ElementType.FIELD) // BECAUSE EMPLOYEE ROLE IS A FIELD IN DTO
public @interface EmployeeRoleValidation {

    // Below three things are copied from any random annotation class
    String message() default "Role of Employee can either be USER or ADMIN";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

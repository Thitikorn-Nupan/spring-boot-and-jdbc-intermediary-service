package com.ttknp.springbootandjdbcintermediaryservice.controllers.h2_shop;


import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.Employee;
import com.ttknp.springbootandjdbcintermediaryservice.services.h2_shop.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/employee", "/employees"})
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping(value = {"/", "/selectAll"})
    private ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(employeeService.getAllEmployees());
    }

    @PostMapping(value = "/save")
    private ResponseEntity<Integer> saveEmployee(@RequestBody Employee employee) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(employeeService.saveEmployee(employee));
    }

    @PutMapping(value = "/edit")
    private ResponseEntity<Integer> editEmployee(@RequestBody Employee employee) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(employeeService.editEmployee(employee));
    }
}

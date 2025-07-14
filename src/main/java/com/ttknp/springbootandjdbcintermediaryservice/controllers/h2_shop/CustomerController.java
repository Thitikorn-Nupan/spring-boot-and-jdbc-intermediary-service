package com.ttknp.springbootandjdbcintermediaryservice.controllers.h2_shop;


import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.Customer;
import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.Employee;
import com.ttknp.springbootandjdbcintermediaryservice.services.h2_shop.CustomerService;
import com.ttknp.springbootandjdbcintermediaryservice.services.h2_shop.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/customer", "/customers"})
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping(value = {"/", "/selectAll"})
    private ResponseEntity<List<Customer>> getAllEmployees() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.getAllCustomers());
    }

    @PostMapping(value = "/save")
    private ResponseEntity<Integer> saveEmployee(@RequestBody Customer customer) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.saveCustomer(customer));
    }

    @PutMapping(value = "/edit")
    private ResponseEntity<Integer> editEmployee(@RequestBody Customer customer) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.editCustomer(customer));
    }
}

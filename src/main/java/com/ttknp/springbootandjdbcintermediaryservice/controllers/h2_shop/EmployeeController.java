package com.ttknp.springbootandjdbcintermediaryservice.controllers.h2_shop;


import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.Employee;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_order_by.SqlOrderByHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_order_by.entity.RequestOrderBy;
import com.ttknp.springbootandjdbcintermediaryservice.services.h2_shop.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = {"/employee", "/employees"})
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
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

    /**
     If @RequestBody List<OrderBy> orderBy you can pass json like this
     [
         {
             "column": "birthday",
             "direction": "asc"
         },
         {
             "column": "full_name",
             "direction": "desc"
         }
     ]
     <p>
     If @RequestBody RequestOrderBy requestOrderBy you can pass json like this
     {
     "length": 10,
     "orderBy": [
             {
                 "column": "birthday",
                 "direction": "asc"
             },
             {
                 "column": "full_name",
                 "direction": "desc"
             }
         ]
     }
     */
    @GetMapping(value = "/selectAllOrderBy")
    private ResponseEntity<List<Employee>> getAllCustomersOrderBy(@RequestBody(required = false) RequestOrderBy requestOrderBy) {
        SqlOrderByHelper<Employee> sqlOrderByHelper = null;
        if (requestOrderBy != null) {
            if (!requestOrderBy.getOrderBy().isEmpty()) {
                sqlOrderByHelper = ((stringBuilder, alias, model) -> {
                    /*
                      See description on CustomerController & JdbcSelectHelper
                      This is lambda expression for implement SqlOrderByHelper interface, Note! sqlOrderByHelper = ((...)) -> {...}); means implement interface  appendOrderBy(...) method
                      Again ! Not work yet at the first time If you see log it switched 2 classes as JdbcSelectHelper & CustomerController
                    */
                    String orderByAsString = requestOrderBy.getOrderBy(alias, requestOrderBy.getOrderBy());
                    stringBuilder.append(orderByAsString);
                });
            }
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(employeeService.getAllEmployeesOrderBy(sqlOrderByHelper));
    }
}

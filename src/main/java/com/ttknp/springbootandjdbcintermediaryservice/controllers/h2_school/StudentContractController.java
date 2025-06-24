package com.ttknp.springbootandjdbcintermediaryservice.controllers.h2_school;

import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.StudentContract;
import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.Teacher;
import com.ttknp.springbootandjdbcintermediaryservice.services.h2_school.StudentContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/studentContract","/studentContracts"})
public class StudentContractController {

    private final StudentContractService studentContractService;

    @Autowired
    public StudentContractController(StudentContractService studentContractService) {
        this.studentContractService = studentContractService;
    }

    @GetMapping(value = {"/", "/selectAll"})
    private ResponseEntity<List<StudentContract>> getStudentsContracts() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentContractService.getAllStudentContracts());
    }


    @PostMapping(value = "/save")
    private ResponseEntity<Integer> saveStudentContract(@RequestBody StudentContract studentContract) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentContractService.saveStudentContract(studentContract));
    }

    @PostMapping(value = "/reload")
    private ResponseEntity<Integer> reloadStudents() {
        studentContractService.reloadStudentsContracts();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(1);
    }

}

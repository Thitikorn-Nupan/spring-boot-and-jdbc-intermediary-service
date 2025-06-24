package com.ttknp.springbootandjdbcintermediaryservice.controllers.h2_school;

import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.Student;
import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.Teacher;
import com.ttknp.springbootandjdbcintermediaryservice.services.h2_school.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/teacher","/teachers"})
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping(value = {"/", "/selectAll"})
    private ResponseEntity<List<Teacher>> getTeachers() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(teacherService.getAllTeachers());
    }


    @PostMapping(value = "/save")
    private ResponseEntity<Integer> saveTeacher(@RequestBody Teacher teacher) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(teacherService.saveTeacher(teacher));
    }

    @PutMapping(value = "/edit")
    private ResponseEntity<Integer> editTeacher(@RequestBody Teacher teacher) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(teacherService.editTeacher(teacher));
    }
}

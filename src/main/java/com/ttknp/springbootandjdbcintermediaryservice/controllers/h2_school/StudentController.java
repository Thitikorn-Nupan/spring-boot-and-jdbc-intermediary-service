package com.ttknp.springbootandjdbcintermediaryservice.controllers.h2_school;

import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.Student;
import com.ttknp.springbootandjdbcintermediaryservice.services.h2_school.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"/student", "/students"})
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }




    @GetMapping(value = {"/", "/selectAll"})
    private ResponseEntity<List<Student>> getStudents() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.getAllStudents());
    }

    @GetMapping(value = "/selectAllAsMap")
    private ResponseEntity<List<Map<String, Object>>> getStudentsAsMap() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.getAllStudentsAsMap());
    }

    @GetMapping(value = "/selectAllWhereLikeByLevel")
    private ResponseEntity<List<Student>> getStudentsByLevel(@RequestParam("value") String value) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.getAllStudentsByLevel(value));
    }

    @GetMapping(value = "/selectAllOnlyColumnFullNameWhereLikeLevel")
    private ResponseEntity<List<String>> getFullNameStudentsWhereLikeLevel(@RequestParam("value") String value) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.getAllStudentsFullNameWhereLikeLevel(value));
    }

    @GetMapping(value = "/selectAllOnlyColumnBy")
    private ResponseEntity<List<?>> getPropertyStudentsBy(@RequestParam("property") String property) {
        // new writing of switch case
        List<?> propStudents = switch (property) {
            case "level" -> studentService.getAllStudentsLevel();
            case "full_name" -> studentService.getAllStudentsFullName();
            case "birthday" -> studentService.getAllStudentsDateOfBirth();
            case "id" -> studentService.getAllStudentsID();
            default -> new ArrayList<>();
        };
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(propStudents);
    }

    @GetMapping(value = "/selectOneById")
    private ResponseEntity<Student> getStudentById(@RequestParam("id") Long id, @RequestParam("con") Boolean con) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.getStudentById(con, id));
    }

    @GetMapping(value = "/selectOneOnlyColumnBy")
    private ResponseEntity<?> getPropertyStudentBy(@RequestParam("property") String property, @RequestParam("id") Long id) {
        Object PropertyStudent = switch (property) {
            case "level" -> studentService.getStudentLevelById(id);
            case "full_name" -> studentService.getStudentFullNameById(id);
            default -> null;
        };
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(PropertyStudent);
    }

    @GetMapping(value = "/selectTotal")
    private ResponseEntity<Integer> getTotalStudents() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.getTotalStudents());
    }

    @GetMapping(value = "/selectTotalByFullName")
    private ResponseEntity<Integer> getTotalStudents(@RequestParam("value") String value) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.getTotalStudentsByFullName(value));
    }





    @PostMapping(value = "/reload")
    private ResponseEntity<Integer> reloadStudents() {
        studentService.reloadStudents();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(1);
    }

    @PostMapping(value = "/saveDemo")
    private ResponseEntity<Integer> saveStudent(@RequestParam("id") Long id,@RequestParam("fullName") String fullName) {
        studentService.saveStudentDemo(id,fullName);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(1);
    }

    @PostMapping(value = "/save")
    private ResponseEntity<Integer> saveStudent(@RequestBody Student student, @RequestParam("method") String method) {
        Integer rowAffected = 0;
        // old writing of switch case
        switch (method) {
            case "saveStudentSimpleInsert":
                rowAffected = studentService.saveStudentSimpleInsert(student);
                break;
            case "saveStudent":
                rowAffected = studentService.saveStudent(student);
                break;
            case "saveStudentApplyAnnotationOwnMapParams":
                rowAffected = studentService.saveStudentApplyAnnotationOwnMapParams(student);
                break;
            case "saveStudentApplyAnnotationAutoMapParams":
                rowAffected = studentService.saveStudentApplyAnnotationAutoMapParams(student);
                break;
            default:
                break;
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(rowAffected);
    }




    @PutMapping(value = "/edit")
    private ResponseEntity<Integer> editStudent(@RequestBody Student student, @RequestParam("uniqKey") String uniqKey,@RequestParam("method") String method) {
        Integer rowAffected = 0;
        // old writing of switch case
        switch (method) {
            case "editStudentApplyAnnotationOwnMapParams":
                rowAffected = studentService.editStudentApplyAnnotationOwnMapParams(student,uniqKey);
                break;
            case "editStudentApplyAnnotationAutoMapParams":
                rowAffected = studentService.editStudentApplyAnnotationAutoMapParams(student,uniqKey);
                break;
            default:
                break;
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(rowAffected);
    }




    @DeleteMapping(value = "/removeOneById")
    private ResponseEntity<Integer> removeStudentById(@RequestParam("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.removeStudentById(id));
    }

    @DeleteMapping(value = "/removeBackupOneById")
    private ResponseEntity<Integer> removeBackupStudentById(@RequestParam("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(studentService.removeBackupStudentById(id));
    }


}

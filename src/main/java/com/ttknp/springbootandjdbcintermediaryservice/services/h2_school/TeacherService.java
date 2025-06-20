package com.ttknp.springbootandjdbcintermediaryservice.services.h2_school;

import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.Student;
import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.Teacher;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete.JdbcInsertUpdateDeleteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.select.JdbcSelectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final JdbcInsertUpdateDeleteHelper<Teacher> jdbcInsertUpdateDeleteHelper;
    private final JdbcSelectHelper<Teacher> jdbcSelectHelper;
    @Autowired
    public TeacherService(JdbcInsertUpdateDeleteHelper<Teacher> jdbcInsertUpdateDeleteHelper, JdbcSelectHelper<Teacher> jdbcSelectHelper) {
        this.jdbcInsertUpdateDeleteHelper = jdbcInsertUpdateDeleteHelper;
        this.jdbcSelectHelper = jdbcSelectHelper;
    }


    public List<Teacher> getAllTeachers() {
        return jdbcSelectHelper.selectAll(Teacher.class);
    }



    public Integer saveTeacher(Teacher teacher) {
        return jdbcInsertUpdateDeleteHelper.insertOne(
                Teacher.class,
                new String[]{"tid"},
                teacher.getFullName(),
                teacher.getBirthday(),
                teacher.getClassId()
        );
    }
}

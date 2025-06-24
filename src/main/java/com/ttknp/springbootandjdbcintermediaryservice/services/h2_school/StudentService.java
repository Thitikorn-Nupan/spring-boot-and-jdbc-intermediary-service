package com.ttknp.springbootandjdbcintermediaryservice.services.h2_school;


import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.Student;
// import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete.JdbcInsertUpdateDeleteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete.JdbcInsertUpdateDeleteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.select.JdbcSelectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final JdbcSelectHelper<Student> jdbcSelectHelper;
    private final JdbcInsertUpdateDeleteHelper<Student> jdbcInsertUpdateDeleteHelper;

    @Autowired
    public StudentService(JdbcSelectHelper<Student> jdbcSelectHelper,JdbcInsertUpdateDeleteHelper<Student> jdbcInsertUpdateDeleteHelper) {
        this.jdbcSelectHelper =  jdbcSelectHelper;
        this.jdbcInsertUpdateDeleteHelper = jdbcInsertUpdateDeleteHelper;
    }

    /**
    public List<Student> getAllStudents() {
        return jdbcSelectHelper.selectBoth(Student.class,new StudentListResultSetExtractor());
    }
    public List<Student> getAllStudentsByLevel(String level) {
        return jdbcSelectHelper.selectBothWhereLikeAColumn(Student.class, new StudentListResultSetExtractor(),"level", level);
    }
    */

    public List<Map<String, Object>> getAllStudentsAsMap() {
        return jdbcSelectHelper.selectAllAsMap(Student.class);
    }

    public List<Student> getAllStudents() {
        return jdbcSelectHelper.selectAll(Student.class);
    }

    public List<Student> getAllStudentsByLevel(String level) {
        return jdbcSelectHelper.selectAllWhereLikeAColumn(Student.class, "level", level);
    }

    public List<Long> getAllStudentsID() {
        return jdbcSelectHelper.selectAllOnlyColumn(Student.class,Long.class,"SID");
    }

    public List<Long> getAllStudentsIDWhereLikeLevel(String level) {
        return jdbcSelectHelper.selectAllOnlyColumnWhereLikeAColumn(Student.class,Long.class,"SID","LEVEL",level);
    }

    public List<String> getAllStudentsFullName() {
        return jdbcSelectHelper.selectAllOnlyColumn(Student.class,String.class,"FULL_NAME");
    }

    public List<String> getAllStudentsFullNameWhereLikeLevel(String level) {
        return jdbcSelectHelper.selectAllOnlyColumnWhereLikeAColumn(Student.class,String.class,"FULL_NAME","LEVEL",level);
    }

    public List<String> getAllStudentsLevel() {
        return jdbcSelectHelper.selectAllOnlyColumn(Student.class,String.class,"LEVEL");
    }

    public List<Date> getAllStudentsDateOfBirth() {
        return jdbcSelectHelper.selectAllOnlyColumn(Student.class,Date.class,"BIRTHDAY");
    }





    public Student getStudentById(Boolean con,Long id) {
        return con ? jdbcSelectHelper.selectOne(Student.class,"SID",id) : jdbcSelectHelper.selectOne("SELECT * FROM H2_SCHOOL.STUDENTS WHERE SID = ?;",Student.class,id);
    }

    public String getStudentFullNameById(Long id) {
        return jdbcSelectHelper.selectOneOnlyColumn(Student.class, String.class,"FULL_NAME","SID",id);
    }

    public String getStudentLevelById(Long id) {
        return jdbcSelectHelper.selectOneOnlyColumn(Student.class, String.class,"LEVEL","SID",id);
    }

    public Integer getTotalStudents() {
        return jdbcSelectHelper.selectCount(Student.class);
    }

    public Integer getTotalStudentsByFullName(String fullName) {
        return jdbcSelectHelper.selectCount(Student.class,"FULL_NAME",fullName);
    }






    public Integer saveStudent(Student student) {
        return jdbcInsertUpdateDeleteHelper.insertOne(
                Student.class,
                new String[]{"sid","teacherName"},
                student.getFullName(),
                student.getBirthday(),
                student.getLevel()
        );
    }

    public Integer saveStudentSimpleInsert(Student student) {
        return jdbcInsertUpdateDeleteHelper.insertOne(
                Student.class,
                "teacherName",
                student
        );
    }

    public Integer saveStudentApplyAnnotationOwnMapParams(Student student) {
        return jdbcInsertUpdateDeleteHelper.insertOne(
                Student.class,
                student.getFullName(),
                student.getBirthday(),
                student.getLevel()
        );
    }

    public Integer saveStudentApplyAnnotationAutoMapParams(Student student) {
        try {
            return jdbcInsertUpdateDeleteHelper.insertOne(
                    Student.class,
                    student
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveStudentDemo(Long id,String fullName) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("{SID}",id);
        params.put("{FULL_NAME}",fullName);
        params.put("{BIRTHDAY}","1999-01-01");
        params.put("{LEVEL}","B+");
        jdbcInsertUpdateDeleteHelper.executeStatement("demo_insert_student.sql",params);
    }




    public Integer editStudentApplyAnnotationOwnMapParams(Student student,String uniqKey) {
        return jdbcInsertUpdateDeleteHelper.updateOne(
                Student.class,
                uniqKey,
                student.getFullName(),
                student.getBirthday(),
                student.getLevel(),
                student.getSid()
        );
    }

    public Integer editStudentApplyAnnotationAutoMapParams(Student student,String uniqKey) {
        try {
            return jdbcInsertUpdateDeleteHelper.updateOne(
                    Student.class,
                    uniqKey,
                    student
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }




    public Integer removeStudentById(Long id) {
        try {
            return jdbcInsertUpdateDeleteHelper.deleteOne(Student.class,"SID",id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer removeBackupStudentById(Long id) {
        try {
            if (jdbcSelectHelper.selectCount(Student.class,"SID",id) > 0) {
                // log.debug("Found student with id {}",id);
                HashMap<String,Object> params = new HashMap<>();
                params.put("{SID}",id);
                params.put("[SID]","SID");
                jdbcInsertUpdateDeleteHelper.executeStatement("backup_delete_student.sql",params);
                return jdbcInsertUpdateDeleteHelper.deleteOne(Student.class,"SID",id);
            }
          return 0;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }




    public void reloadStudents() {
        // can truncate because it has a relation table
    }




    private static class StudentListResultSetExtractor implements ResultSetExtractor<List<Student>> {
        @Override
        public List<Student> extractData(ResultSet rs) throws SQLException {
            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                Long sid = rs.getLong("SID");
                String fullName = rs.getString("FULL_NAME");
                Date birthday = rs.getDate("BIRTHDAY");
                String level = rs.getString("LEVEL");
                students.add(new Student(sid,fullName,birthday,level));
            }
            return students;
        }
    }
}

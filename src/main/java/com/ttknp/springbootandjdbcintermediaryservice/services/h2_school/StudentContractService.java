package com.ttknp.springbootandjdbcintermediaryservice.services.h2_school;

import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.StudentContract;
import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school.StudentContract;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete.JdbcInsertUpdateDeleteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.select.JdbcSelectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentContractService {

    private final JdbcInsertUpdateDeleteHelper<StudentContract> jdbcInsertUpdateDeleteHelper;
    private final JdbcSelectHelper<StudentContract> jdbcSelectHelper;

    @Autowired
    public StudentContractService(JdbcInsertUpdateDeleteHelper<StudentContract> jdbcInsertUpdateDeleteHelper, JdbcSelectHelper<StudentContract> jdbcSelectHelper) {
        this.jdbcInsertUpdateDeleteHelper = jdbcInsertUpdateDeleteHelper;
        this.jdbcSelectHelper = jdbcSelectHelper;
    }


    public List<StudentContract> getAllStudentContracts() {
        return jdbcSelectHelper.selectAll(StudentContract.class);
    }


    /*
    public Integer saveStudentContract(StudentContract studentContract) {
        return jdbcInsertUpdateDeleteHelper.insertOne(
                StudentContract.class,
                new String[]{"cid"},
                studentContract.getSid(),
                studentContract.getContractType(),
                studentContract.getContractStartDate(),
                studentContract.getContractEndDate(),
                studentContract.getParentGuardianName(),
                studentContract.getParentGuardianPhone(),
                studentContract.getParentGuardianAddress(),
                studentContract.getEmergencyContactName(),
                studentContract.getEmergencyContactPhone(),
                studentContract.getContractStatus()
        );
    }
    */

    /*
    // Same
    public Integer saveStudentContract(StudentContract studentContract) {
        return jdbcInsertUpdateDeleteHelper.insertOne(StudentContract.class, "cid",studentContract);
    }
    */

    // Same
    public Integer saveStudentContract(StudentContract studentContract) {
        try {
            return jdbcInsertUpdateDeleteHelper.insertOne(StudentContract.class, studentContract);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadStudentsContracts() {
        jdbcInsertUpdateDeleteHelper.executeStatement("clear_students_contracts.sql");
        jdbcInsertUpdateDeleteHelper.executeStatement("insert_students_contracts.sql");
    }


}

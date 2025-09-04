package com.ttknp.springbootandjdbcintermediaryservice.services.h2_shop;

import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.Employee;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete.JdbcInsertUpdateDeleteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.select.JdbcSelectHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_where_and_order_by.SqlOrderByHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_where_and_order_by.SqlWhereHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final JdbcSelectHelper<Employee> jdbcSelectHelper;
    private final JdbcInsertUpdateDeleteHelper<Employee> jdbcInsertUpdateDeleteHelper;

    @Autowired
    public EmployeeService(JdbcSelectHelper<Employee> jdbcSelectHelper, JdbcInsertUpdateDeleteHelper<Employee> jdbcInsertUpdateDeleteHelper) {
        this.jdbcSelectHelper = jdbcSelectHelper;
        this.jdbcInsertUpdateDeleteHelper = jdbcInsertUpdateDeleteHelper;
    }

    // SqlOrderByHelper is interface class that you can implement on the fly with lambda expression
    public List<Employee> getAllEmployeesOrderBy(SqlOrderByHelper<Employee> sqlOrderByHelper) {
        return jdbcSelectHelper.selectAll(Employee.class, sqlOrderByHelper);
    }
    public List<Employee> getAllEmployeesWhereAndOrderBy(SqlOrderByHelper<Employee> sqlOrderByHelper, SqlWhereHelper<Employee> sqlWhereHelper, Employee employee) {
        return jdbcSelectHelper.selectAll(Employee.class , sqlOrderByHelper, sqlWhereHelper, employee);
    }

    public List<Employee> getAllEmployees() {
        return jdbcSelectHelper.selectAll(Employee.class);
    }

    public Integer saveEmployee(Employee employee) {
        try {
            return jdbcInsertUpdateDeleteHelper.insertOneIsPkSubclass(
                    Employee.class,
                    employee
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer editEmployee(Employee employee) {
        try {
            return jdbcInsertUpdateDeleteHelper.updateOneIsPkSubclass(
                    Employee.class,
                    "id",
                    employee
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

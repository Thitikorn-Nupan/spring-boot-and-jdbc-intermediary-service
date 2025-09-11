package com.ttknp.springbootandjdbcintermediaryservice.services.h2_shop;

import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.Customer;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_where_and_order_by.SqlOrderByHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete.JdbcInsertUpdateDeleteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.select.JdbcSelectHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_where_and_order_by.SqlWhereHelper;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final JdbcSelectHelper<Customer> jdbcSelectHelper;
    private final JdbcInsertUpdateDeleteHelper<Customer> jdbcInsertUpdateDeleteHelper;

    @Autowired
    public CustomerService(JdbcSelectHelper<Customer> jdbcSelectHelper, JdbcInsertUpdateDeleteHelper<Customer> jdbcInsertUpdateDeleteHelper) {
        this.jdbcSelectHelper = jdbcSelectHelper;
        this.jdbcInsertUpdateDeleteHelper = jdbcInsertUpdateDeleteHelper;
    }

    // SqlOrderByHelper is interface class that you can implement on the fly with lambda expression
    public List<Customer> getAllCustomersOrderBy(SqlOrderByHelper<Customer> sqlOrderByHelper) {
        return jdbcSelectHelper.selectAll(Customer.class, sqlOrderByHelper);
    }

    public List<Customer> getAllCustomersOrderByAndReplaceAssignValues(SqlOrderByHelper<Customer> sqlOrderByHelper) {
        StringBuilder stringBuilderSql = jdbcSelectHelper.getStatement("select_star_customers.sql");
        // log.debug("stringBuilderSql : {}", stringBuilderSql.toString()); // SELECT * FROM H2_SHOP.EMPLOYEES [SQL_CONDITION];
        return jdbcSelectHelper.selectAll(Customer.class,stringBuilderSql, sqlOrderByHelper);
    }

    public List<Customer> getAllCustomersWhereAndOrderBy(SqlOrderByHelper<Customer> sqlOrderByHelper, SqlWhereHelper<Customer> sqlWhereHelper, Customer customer) {
        return jdbcSelectHelper.selectAll(Customer.class , sqlOrderByHelper, sqlWhereHelper, customer);
    }

    public List<Customer> getAllCustomersWhereAndOrderByCustomAlias(SqlOrderByHelper<Customer> sqlOrderByHelper, SqlWhereHelper<Customer> sqlWhereHelper, Customer customer, @Nullable String customAlias) {
        return jdbcSelectHelper.selectAll(Customer.class ,customAlias, sqlOrderByHelper, sqlWhereHelper, customer);
    }

    public List<Customer> getAllCustomers() {
        return jdbcSelectHelper.selectAll(Customer.class);
    }

    public Integer saveCustomer(Customer employee) {
        try {
            return jdbcInsertUpdateDeleteHelper.insertOneIsPkSubclass(
                    Customer.class,
                    employee
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer editCustomer(Customer employee) {
        try {
            return jdbcInsertUpdateDeleteHelper.updateOneIsPkSubclass(
                    Customer.class,
                    "id",
                    employee
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

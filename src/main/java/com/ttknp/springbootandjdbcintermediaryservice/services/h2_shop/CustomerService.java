package com.ttknp.springbootandjdbcintermediaryservice.services.h2_shop;

import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.Customer;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete.JdbcInsertUpdateDeleteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.select.JdbcSelectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final JdbcSelectHelper<Customer> jdbcSelectHelper;
    private final JdbcInsertUpdateDeleteHelper<Customer> jdbcInsertUpdateDeleteHelper;

    @Autowired
    public CustomerService(JdbcSelectHelper<Customer> jdbcSelectHelper, JdbcInsertUpdateDeleteHelper<Customer> jdbcInsertUpdateDeleteHelper) {
        this.jdbcSelectHelper = jdbcSelectHelper;
        this.jdbcInsertUpdateDeleteHelper = jdbcInsertUpdateDeleteHelper;
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

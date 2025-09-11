package com.ttknp.springbootandjdbcintermediaryservice.controllers.h2_shop;


import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_where_and_order_by.SqlWhereHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_where_and_order_by.entity.RequestOrderBy;
import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.Customer;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_where_and_order_by.SqlOrderByHelper;
import com.ttknp.springbootandjdbcintermediaryservice.services.h2_shop.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/customer", "/customers"})
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping(value = {"/", "/selectAll"})
    private ResponseEntity<List<Customer>> getAllEmployees() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.getAllCustomers());
    }

    @PostMapping(value = "/save")
    private ResponseEntity<Integer> saveEmployee(@RequestBody Customer customer) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.saveCustomer(customer));
    }

    @PutMapping(value = "/edit")
    private ResponseEntity<Integer> editEmployee(@RequestBody Customer customer) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.editCustomer(customer));
    }




    /**
     // All below are example json you can pass in postman body raw json/application
     // Case no order by no where or set body as none
     <br/>
     {

     }
     <br/>
     // Case has order by no where
     <br/>
     {
         "length" :10,
         "orderBy" : [
                     {
                        "column": "level",
                        "direction": "asc"
                     }
                     ,
                     {
                        "column": "full_name",
                        "direction": "asc"
                     }
                     ,
                     {
                        "column": "birthday",
                        "direction": "asc"
                     }
        ]
     }
     <br/>
     // Case no order by has where
     <br/>
     {
         "length" :10,
         "whereModel": {
            "id": "4b5f0b77-7ad3-4d22-b9cb-c8b2bf526d27",
            "birthday": "1989-01-29",
            "level": "A+"
        }
     }
     <br/>
     // Case has order by has where
     {
         "length" :10,
         "orderBy" : [
             {
                 "column": "level",
                 "direction": "asc"
             }
             ,
             {
                 "column": "full_name",
                 "direction": "desc"
             }
             ,
             {
                 "column": "birthday",
                 "direction": "asc"
             }
        ],
         "whereModel": {
            "fullName": "Lon Slider",
            "birthday": "1989-01-29",
            "level": "A+"
         }
     }
     <br/>
     */
    @GetMapping(value = "/selectAllOrderBy")
    private ResponseEntity<List<Customer>> getAllCustomersOrderBy(@RequestBody(required = false) RequestOrderBy<Customer> requestOrderBy) {
        SqlOrderByHelper<Customer> sqlOrderByHelper = null;
        if (requestOrderBy != null) {
            // log.debug(" orderBy (list) = {}", requestOrderBy.getOrderBy()); // [OrderBy{column='level', direction='desc'}, OrderBy{column='full_name', direction='asc'}]
            if (!requestOrderBy.getOrderBy().isEmpty()) {
                log.debug("orderBy & requestOrderBy are not null"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 1
                sqlOrderByHelper = ((stringBuilder, alias, model) -> {
                    /*
                      This is lambda expression for implement SqlOrderByHelper interface, Note! sqlOrderByHelper = ((...)) -> {...}); means implement interface  appendOrderBy(...) method
                      Again ! Not work yet at the first time If you see log it switched 2 classes as JdbcSelectHelper & CustomerController
                    */
                    // ***** c.t.s.helpers.jdbc.select.JdbcSelectHelper Do 2 : before stringBuilder thru implement = select * from h2_shop.customers
                    // log.debug("*** before stringBuilder on implement : {}", stringBuilder.toString()); // ***** c.t.s.controllers.h2_shop.CustomerController Do 3 : *** before stringBuilder on implement : select * from h2_shop.customers as alias order by
                    String orderByAsString = requestOrderBy.getOrderBy(alias, requestOrderBy.getOrderBy());
                    // log.debug("get orderBy as string = {}", orderByAsString); // ***** c.t.s.controllers.h2_shop.CustomerController Do 4 : get orderBy as string =  alias.level desc, alias.full_name asc limit 3
                    stringBuilder.append(orderByAsString);
                    // ***** c.t.s.helpers.jdbc.select.JdbcSelectHelper Do 5 : after stringBuilder thru implement = select * from h2_shop.customers as alias order by  alias.level desc, alias.full_name asc limit 3
                });
            }
            else { // Optional else
                log.debug("orderBy & requestOrderBy are null");
            }
        }
        else {  // On postman set body to none for no request body
            log.debug("requestOrderBy is null");
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.getAllCustomersOrderBy(sqlOrderByHelper));
    }

    @GetMapping(value = "/selectAllOrderByAndReplaceAssignValues")
    private ResponseEntity<List<Customer>> getAllCustomersOrderByAndReplaceAssignValues(@RequestBody(required = true) RequestOrderBy<Customer> requestOrderBy) {
        SqlOrderByHelper<Customer> sqlOrderByHelper = null;
        if (requestOrderBy != null) {
            if (requestOrderBy.getOrderBy() != null && !requestOrderBy.getOrderBy().isEmpty()) {
                sqlOrderByHelper = ((stringBuilder, alias, model) -> {
                    String orderByAsString = requestOrderBy.getOrderBy(alias, requestOrderBy.getOrderBy());
                    stringBuilder.append(orderByAsString);
                });
            }
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.getAllCustomersOrderByAndReplaceAssignValues(sqlOrderByHelper));
    }

    @GetMapping(value = "/selectAllWhereAndOrderBy")
    private ResponseEntity<List<Customer>> getAllCustomersWhereAndOrderBy(@RequestBody(required = false) RequestOrderBy<Customer> requestOrderBy) {
        SqlWhereHelper<Customer> sqlWhereHelper = null;
        SqlOrderByHelper<Customer> sqlOrderByHelper = null;
        Customer customer = requestOrderBy.getWhereModel() != null ? requestOrderBy.getWhereModel() : null;
        if (requestOrderBy != null) {
            if (requestOrderBy.getWhereModel() != null) {
                sqlWhereHelper = ((stringBuilder, alias, model) -> {
                    String whereAsString = requestOrderBy.getWhereModelIsPkSubclass(alias); // Case class have primary key on subclass
                    // String whereAsString = requestOrderBy.getWhereModel(alias); // Case class have no primary key on subclass
                    stringBuilder.append(whereAsString);
                    log.debug("sql after append where = {}",stringBuilder.toString());
               });
            }

            if (requestOrderBy.getOrderBy() != null && !requestOrderBy.getOrderBy().isEmpty()) {
                sqlOrderByHelper = ((stringBuilder, alias, model) -> {
                    String orderByAsString = requestOrderBy.getOrderBy(alias, requestOrderBy.getOrderBy());
                    stringBuilder.append(orderByAsString);
                    log.debug("sql after append order by = {}",stringBuilder.toString());
                });
            }
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.getAllCustomersWhereAndOrderBy(sqlOrderByHelper, sqlWhereHelper, customer));
    }

    @GetMapping(value = "/selectAllWhereAndOrderByCustomAlias")
    private ResponseEntity<List<Customer>> getAllCustomersWhereAndOrderByCustomAlias(@RequestBody(required = false) RequestOrderBy<Customer> requestOrderBy) {
        SqlWhereHelper<Customer> sqlWhereHelper = null;
        SqlOrderByHelper<Customer> sqlOrderByHelper = null;
        Customer customer = requestOrderBy.getWhereModel() != null ? requestOrderBy.getWhereModel() : null;
        if (requestOrderBy != null) {
            if (requestOrderBy.getWhereModel() != null) {
                sqlWhereHelper = ((stringBuilder, alias, model) -> {
                    String whereAsString = requestOrderBy.getWhereModelIsPkSubclassCustomAlias(alias);
                    stringBuilder.append(whereAsString);
                    log.debug("sql after append where = {}",stringBuilder.toString());
                });
            }

            if (requestOrderBy.getOrderBy() != null && !requestOrderBy.getOrderBy().isEmpty()) {
                sqlOrderByHelper = ((stringBuilder, alias, model) -> {
                    String orderByAsString = requestOrderBy.getOrderByCustomAlias(alias, requestOrderBy.getOrderBy());
                    stringBuilder.append(orderByAsString);
                    log.debug("sql after append order by = {}",stringBuilder.toString());
                });
            }
        }
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(customerService.getAllCustomersWhereAndOrderByCustomAlias(sqlOrderByHelper, sqlWhereHelper, customer,"c"));
    }


}

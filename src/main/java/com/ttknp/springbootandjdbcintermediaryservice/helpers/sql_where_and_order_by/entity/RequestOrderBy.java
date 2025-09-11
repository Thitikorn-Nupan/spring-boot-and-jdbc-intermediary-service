package com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_where_and_order_by.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.mapping.Column;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

public class RequestOrderBy <T> {

    private static final Logger log = LoggerFactory.getLogger(RequestOrderBy.class);
    private Integer length;
    private List<OrderBy> orderBy;
    private T whereModel; // optional where model

    public RequestOrderBy(Integer length, List<OrderBy> orderBy, T whereModel) {
        this.length = length;
        this.orderBy = orderBy;
        this.whereModel = whereModel;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<OrderBy> orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy(String alias, List<OrderBy> orderBy) {
        StringBuilder stringBuilder = new StringBuilder();
        if (orderBy.size() == 1) { // case one order by
            OrderBy orderByItem = orderBy.get(0); // get order as object
            compactColumnAndDirection(stringBuilder, alias, orderByItem); // after compactColumnAndDirection(...) done stringBuilder changed
        }
        else { // case many order by
            for (int i = 0; i < orderBy.size(); i++) {
                OrderBy orderByItem = orderBy.get(i);
                compactColumnAndDirection(stringBuilder, alias, orderByItem);
                if (i + 1 < orderBy.size()) {  // add comma if not last item
                    stringBuilder.append(",");
                }
            }
        }
        if (length != null) {
            stringBuilder
                    .append(" limit ")
                    .append(length);
        }
        return stringBuilder.toString();
    }

    public String getOrderByCustomAlias(String alias, List<OrderBy> orderBy) {
        StringBuilder stringBuilder = new StringBuilder();

        // Note ! you set alias if null will be " " on selectAll(Class<T> aBeanClass, @Nullable String customAlias , SqlOrderByHelper<T> sqlOrderByHelper, SqlWhereHelper<T> sqlWhereHelper, T model)

        if (orderBy.size() == 1) { // case one order by
            OrderBy orderByItem = orderBy.get(0); // get order as object
            if (" ".equals(alias)) {
                // log.debug("alias is null or empty"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 6 : alias is null or empty
                compactColumnAndDirection(stringBuilder, orderByItem); // after compactColumnAndDirection(...) done stringBuilder changed
            } else {
                // log.debug("alias is not null or empty"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 7 : alias is not null or empty
                compactColumnAndDirection(stringBuilder, alias, orderByItem); // after compactColumnAndDirection(...) done stringBuilder changed
            }
        }
        else { // case many order by
            for (int i = 0; i < orderBy.size(); i++) {
                OrderBy orderByItem = orderBy.get(i);
                if (" ".equals(alias)) {
                    // log.debug("alias is null or empty"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 6 : alias is null or empty
                    compactColumnAndDirection(stringBuilder, orderByItem); // after compactColumnAndDirection(...) done stringBuilder changed
                } else {
                    // log.debug("alias is not null or empty"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 7 : alias is not null or empty
                    compactColumnAndDirection(stringBuilder, alias, orderByItem); // after compactColumnAndDirection(...) done stringBuilder changed
                }
                if (i + 1 < orderBy.size()) {  // add comma if not last item
                    stringBuilder.append(",");
                }
            }
        }
        if (length != null) {
            stringBuilder
                    .append(" limit ")
                    .append(length);
        }
        return stringBuilder.toString();
    }

    public T getWhereModel() {
        return whereModel;
    }

    public void setWhereModel(T whereModel) {
        this.whereModel = whereModel;
    }

    ///  Description of process get key and value from object see on JdbcInsertUpdateDeleteHelper->Dynamic insert statement
    public String getWhereModel(String alias) {
        StringBuilder stringBuilder = new StringBuilder();
        Class<T> aClass = (Class<T>) whereModel.getClass();
        Field[] fields = aClass.getDeclaredFields();
        compactColumnAndAssignValue(stringBuilder, alias, fields);
        // log.debug("sql = {}",stringBuilder.toString()); // and alias.fullName = {fullName} and alias.level = {level}
        if (length != null && orderBy == null) { // only limit when no order by
            stringBuilder
                    .append(" limit ")
                    .append(length);
        }
        return stringBuilder.toString();
    }

    public String getWhereModelCustomAlias(String alias) {
        StringBuilder stringBuilder = new StringBuilder();
        Class<T> aClass = (Class<T>) whereModel.getClass();
        Field[] fields = aClass.getDeclaredFields();
        // Now fields already works
        if (" ".equals(alias)) {
            // log.debug("alias is null or empty"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 6 : alias is null or empty
            compactColumnAndAssignValue(stringBuilder, fields);
        } else {
            // log.debug("alias is not null or empty"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 7 : alias is not null or empty
            compactColumnAndAssignValue(stringBuilder, alias, fields);
        }
        // log.debug("sql = {}",stringBuilder.toString()); // and alias.fullName = {fullName} and alias.level = {level}
        if (length != null && orderBy == null) { // only limit when no order by
            stringBuilder
                    .append(" limit ")
                    .append(length);
        }
        return stringBuilder.toString();
    }

    public String getWhereModelIsPkSubclass(String alias) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Field> fields = new ArrayList<>();
        Class<T> aClass = (Class<T>) whereModel.getClass();

        if (aClass.getSuperclass() != null) {
            Class<?> superclass = aClass.getSuperclass();
            for (Field field : superclass.getDeclaredFields()) {
                fields.add(field);
            }
        }

        for (Field field : aClass.getDeclaredFields()) {
            fields.add(field);
        }

        // Now fields already works
        compactColumnAndAssignValue(stringBuilder,alias,fields);



        // log.debug("sql = {}",stringBuilder.toString()); // and alias.fullName = {fullName} and alias.level = {level}
        if (length != null && orderBy == null) { // only limit when no order by
            stringBuilder
                    .append(" limit ")
                    .append(length);
        }

        return stringBuilder.toString();
    }

    public String getWhereModelIsPkSubclassCustomAlias(String alias) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Field> fields = new ArrayList<>();
        Class<T> aClass = (Class<T>) whereModel.getClass();

        if (aClass.getSuperclass() != null) {
            Class<?> superclass = aClass.getSuperclass();
            for (Field field : superclass.getDeclaredFields()) {
                fields.add(field);
            }
        }

        for (Field field : aClass.getDeclaredFields()) {
            fields.add(field);
        }

        // Now fields already works
        if (" ".equals(alias)) {
            // log.debug("alias is null or empty"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 6 : alias is null or empty
            compactColumnAndAssignValue(stringBuilder, fields);
        } else {
            // log.debug("alias is not null or empty"); // ***** c.t.s.controllers.h2_shop.CustomerController Do 7 : alias is not null or empty
            compactColumnAndAssignValue(stringBuilder, alias, fields);
        }

        // log.debug("sql = {}",stringBuilder.toString()); // and alias.fullName = {fullName} and alias.level = {level}
        if (length != null && orderBy == null) { // only limit when no order by
            stringBuilder
                    .append(" limit ")
                    .append(length);
        }

        return stringBuilder.toString();
    }

    private void compactColumnAndAssignValue(StringBuilder stringBuilder, String alias, List<Field> fields){
        this.compactColumnAndAssignValue(stringBuilder, alias, fields.toArray(new Field[0]));
    }

    private void compactColumnAndAssignValue(StringBuilder stringBuilder, List<Field> fields){
        this.compactColumnAndAssignValue(stringBuilder,fields.toArray(new Field[0]));
    }

    private void compactColumnAndAssignValue(StringBuilder stringBuilder, String alias, Field[] fields) {
        for (Field field : fields) {
            field.setAccessible(true);
            Column columnAnnotation = null;
            String name = null;
            Object value = null;

            if (field.isAnnotationPresent(Column.class)) {
                columnAnnotation = field.getAnnotation(Column.class);
            }
            if (columnAnnotation != null) {
                name = columnAnnotation.value();
            }
            else {
                name = field.getName();
            }

            try {
                value = field.get(whereModel);
                if (value != null) {
                    stringBuilder
                            .append(" and ")
                            .append(alias)
                            .append(".")
                            .append(name)
                            .append(" = ")
                            .append("{")
                            .append(name)
                            .append("}")
                            .append(" ");
                }
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
        } // end loop for gen dynamic where
    }

    private void compactColumnAndAssignValue(StringBuilder stringBuilder, Field[] fields) {
        for (Field field : fields) {
            field.setAccessible(true);
            Column columnAnnotation = null;
            String name = null;
            Object value = null;

            if (field.isAnnotationPresent(Column.class)) {
                columnAnnotation = field.getAnnotation(Column.class);
            }
            if (columnAnnotation != null) {
                name = columnAnnotation.value();
            }
            else {
                name = field.getName();
            }

            try {
                value = field.get(whereModel);
                if (value != null) {
                    stringBuilder
                            .append(" and ")
                            .append(name)
                            .append(" = ")
                            .append("{")
                            .append(name)
                            .append("}")
                            .append(" ");
                }
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
        } // end loop for gen dynamic where
    }



    private void compactColumnAndDirection(StringBuilder stringBuilder, String alias, OrderBy orderBy) {
        stringBuilder.append(" ")
                .append(alias)
                .append(".")
                .append(orderBy.getColumn())
                .append(" ")
                .append(orderBy.getDirection());
    }

    // do case if alias is null or empty string
    private void compactColumnAndDirection(StringBuilder stringBuilder, OrderBy orderBy) {
        stringBuilder
                .append(orderBy.getColumn())
                .append(" ")
                .append(orderBy.getDirection());
    }

    public static class OrderBy {

        private String column;
        private String direction; // ASC or DESC

        public OrderBy(String column, String direction) {
            this.column = column;
            this.direction = direction;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("OrderBy{");
            sb.append("column='").append(column).append('\'');
            sb.append(", direction='").append(direction).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}

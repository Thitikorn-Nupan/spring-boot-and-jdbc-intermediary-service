package com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete;

import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.JdbcExecuteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.annotation.IgnoreGenerateSQL;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_common.SQLSyntaxCommon;
import com.ttknp.springbootandjdbcintermediaryservice.services.useful.UsefulGetSQLStatement;
import com.ttknp.springbootandjdbcintermediaryservice.services.useful.UsefulJdbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class JdbcInsertUpdateDeleteHelper<T> extends JdbcExecuteHelper {

    private static final Logger log = LoggerFactory.getLogger(JdbcInsertUpdateDeleteHelper.class);
    private final UsefulJdbcService usefulJdbcService;
    private final UsefulGetSQLStatement usefulGetSQLStatement;

    @Autowired
    public JdbcInsertUpdateDeleteHelper(JdbcTemplate jdbcTemplate, UsefulJdbcService usefulJdbcService,UsefulGetSQLStatement usefulGetSQLStatement) {
        super(jdbcTemplate);
        this.usefulJdbcService = usefulJdbcService;
        this.usefulGetSQLStatement = usefulGetSQLStatement;
    }



    // ------------ Dynamic insert statement own map param (no apply custom annotation) ------------
    public Integer insertOne(Class<T> aBeanClass,String[] fieldsIgnore,Object ...params) {
        StringBuilder stringBuilderSQL,stringBuilderSQLValues;

        stringBuilderSQL = new StringBuilder();
        stringBuilderSQLValues = new StringBuilder();

        stringBuilderSQL.append(SQLSyntaxCommon.INSERT);
        stringBuilderSQL.append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        stringBuilderSQL.append(" (");

        stringBuilderSQLValues.append(" values (");

        Field[] fields = aBeanClass.getDeclaredFields(); // get each props as array of class

        for (int i = 0; i < fields.length ; i++) {

            Field field = fields[i];

            boolean foundIgnoreField = false;

            if (fieldsIgnore != null) {
                foundIgnoreField = Arrays.stream(fieldsIgnore).toList().contains(field.getName());
            }

            if (!foundIgnoreField) {

                Column columnAnnotation = null;
                String fieldName;
                boolean foundCustomField = field.isAnnotationPresent(Column.class);

                if (foundCustomField) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                }
                else {
                    fieldName = field.getName();
                }

                stringBuilderSQL
                        .append(fieldName)
                        .append(" ,");

                stringBuilderSQLValues
                        .append(SQLSyntaxCommon.ASSIGN)
                        .append(" ,");
            }

            if (i == fields.length - 1) { // remove the last comma
                stringBuilderSQL
                        .deleteCharAt(stringBuilderSQL.length() - 1)
                        .append(")");

                stringBuilderSQLValues
                        .deleteCharAt(stringBuilderSQLValues.length() - 1)
                        .append(")");
            }

        } // end for

        stringBuilderSQL
                .append(stringBuilderSQLValues);

        // log.debug("sql insert = {}", stringBuilderSQL.toString());
        return executeUpdateForInsert(stringBuilderSQL.toString(),params);
        // return 1;
    }

    // ------------ Dynamic insert statement own map param (*** apply custom annotation) ------------
    public Integer insertOne(Class<T> aBeanClass, Object ...params) {
        StringBuilder stringBuilderSQL,stringBuilderSQLValues;

        stringBuilderSQL = new StringBuilder();
        stringBuilderSQLValues = new StringBuilder();

        stringBuilderSQL.append(SQLSyntaxCommon.INSERT);
        stringBuilderSQL.append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        stringBuilderSQL.append(" (");

        stringBuilderSQLValues.append(" values (");

        Field[] fields = aBeanClass.getDeclaredFields(); // get each props as array of class

        for (int i = 0; i < fields.length ; i++) {

            Field field = fields[i];

            boolean foundIgnoreField = field.isAnnotationPresent(IgnoreGenerateSQL.class); // check annotation present on prop

            if (!foundIgnoreField) {

                // Note, Don't forget mark @Column("<real field name>") if it's not the same property name and column name
                Column columnAnnotation = null;
                String fieldName;
                boolean foundCustomField = field.isAnnotationPresent(Column.class);

                if (foundCustomField) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                }
                else {
                    fieldName = field.getName();
                }

                stringBuilderSQL
                        .append(fieldName)
                        .append(" ,");

                stringBuilderSQLValues
                        .append(SQLSyntaxCommon.ASSIGN)
                        .append(" ,");
            }

            /*
                Get bug some cases (case one field)
                if (i == fields.length - 1) { // remove the last comma
                    stringBuilderSQL
                            .deleteCharAt(stringBuilderSQL.length() - 1)
                            .append(")");

                    stringBuilderSQLValues
                            .deleteCharAt(stringBuilderSQLValues.length() - 1)
                            .append(")");
                }
            */

        } // end for

        stringBuilderSQL
                .deleteCharAt(stringBuilderSQL.length() - 1)
                .append(")");

        stringBuilderSQLValues
                .deleteCharAt(stringBuilderSQLValues.length() - 1)
                .append(")");

        stringBuilderSQL
                .append(stringBuilderSQLValues);

        return executeUpdateForInsert(stringBuilderSQL.toString(),params);
    }

    // ------------ Dynamic insert statement auto map param (*** apply custom annotation) ------------
    public Integer insertOne(Class<T> aBeanClass, T aBeanObject) throws IllegalAccessException {
        StringBuilder stringBuilderSQL,stringBuilderSQLValues;

        stringBuilderSQL = new StringBuilder();
        stringBuilderSQLValues = new StringBuilder();

        stringBuilderSQL.append(SQLSyntaxCommon.INSERT);
        stringBuilderSQL.append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        stringBuilderSQL.append(" (");

        stringBuilderSQLValues.append(" values (");

        Field[] fields = aBeanClass.getDeclaredFields(); // get each props as array of class
        List<Object> objectsValues = new ArrayList<>();

        for (int i = 0; i < fields.length ; i++) {

            Field field = fields[i];

            if (!field.isAnnotationPresent(IgnoreGenerateSQL.class)) {

                // **** Make the field accessible if it's private
                field.setAccessible(true);
                Column columnAnnotation = null;
                String fieldName;
                Object fieldValue;

                if (field.isAnnotationPresent(Column.class)) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                } else {
                    fieldName = field.getName();
                }

                stringBuilderSQL
                        .append(fieldName)
                        .append(" ,");

                stringBuilderSQLValues
                        .append(SQLSyntaxCommon.ASSIGN)
                        .append(" ,");


                // **** Get the value of the field for the specific POJO instance
                fieldValue = field.get(aBeanObject);
                // log.debug("Field Name: {} , Value: {}" ,fieldName, fieldValue);
                objectsValues.add(fieldValue);
            }

        } // end for

        stringBuilderSQL
                .deleteCharAt(stringBuilderSQL.length() - 1)
                .append(")");

        stringBuilderSQLValues
                .deleteCharAt(stringBuilderSQLValues.length() - 1)
                .append(")");

        stringBuilderSQL
                .append(stringBuilderSQLValues);

        log.debug("sql insert = {}", stringBuilderSQL.toString());
        return executeUpdateForInsert(stringBuilderSQL.toString(),objectsValues);
    }

    // ------------ Auto generate insert statement and map params ------------
    public Integer insertOne(Class<T> aBeanClass,String autoGenerateColumnName,T object){
        String schema = usefulJdbcService.getSchemaNameOnTableAnnotation(aBeanClass);
        String tableName = usefulJdbcService.getTableNameOnTableAnnotation(aBeanClass);

        // cut the propert(ies) has IgnoreGenerateSQL annotation
        List<String> columns = new ArrayList<>();
        Class<T> aClass = (Class<T>) object.getClass();
        Field[] fields = aClass.getDeclaredFields(); // get each props as array of class

        for (Field field : fields) {

            boolean foundIgnoreField = field.isAnnotationPresent(IgnoreGenerateSQL.class); // check annotation present on prop

            if (!foundIgnoreField) {

                Column columnAnnotation = null;
                String fieldName;

                if (field.isAnnotationPresent(Column.class)) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                }
                else {
                    fieldName = field.getName();
                }

                columns.add(fieldName);
            }
        }

        // columns.toArray(new String[0]) it's converting array list to array string
        return executeSimpleInsertByBeanPropertySqlParameterSource(schema,tableName,autoGenerateColumnName, columns.toArray(new String[0]) , object);
    }

    // ------------ Dynamic insert statement auto map params (*** apply custom annotation) *** have subclass ------------
    public Integer insertOneIsPkSubclass(Class<T> aBeanClass, T aBeanObject) throws IllegalAccessException {
        StringBuilder stringBuilderSQL, stringBuilderSQLValues;

        stringBuilderSQL = new StringBuilder();
        stringBuilderSQLValues = new StringBuilder();

        stringBuilderSQL.append(SQLSyntaxCommon.INSERT);
        stringBuilderSQL.append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        stringBuilderSQL.append(" (");

        stringBuilderSQLValues.append(" values (");


        List<Field> fields = new ArrayList<>();

        if (aBeanClass.getSuperclass() != null) { // check if have a subclass then get all fields
            Class<?> superclass = aBeanClass.getSuperclass();
            for (Field field : superclass.getDeclaredFields()) {
                fields.add(field);
            }
        }

        for (Field field : aBeanClass.getDeclaredFields()) { // get all field of main class
            fields.add(field);
        }
        // now field already works

        List<Object> objectsValues = new ArrayList<>();

        for (int i = 0; i < fields.size(); i++) {

            Field field = fields.get(i);

            if (!field.isAnnotationPresent(IgnoreGenerateSQL.class)) {

                // **** Make the field accessible if it's private
                field.setAccessible(true);
                Column columnAnnotation = null;
                String fieldName;
                Object fieldValue;

                if (field.isAnnotationPresent(Column.class)) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                } else {
                    fieldName = field.getName();
                }

                stringBuilderSQL
                        .append(fieldName)
                        .append(" ,");

                stringBuilderSQLValues
                        .append(SQLSyntaxCommon.ASSIGN)
                        .append(" ,");


                // **** Get the value of the field for the specific POJO instance
                fieldValue = field.get(aBeanObject);
                // log.debug("Field Name: {} , Value: {}" ,fieldName, fieldValue);
                objectsValues.add(fieldValue);
            }

        } // end for

        stringBuilderSQL
                .deleteCharAt(stringBuilderSQL.length() - 1)
                .append(")");

        stringBuilderSQLValues
                .deleteCharAt(stringBuilderSQLValues.length() - 1)
                .append(")");

        stringBuilderSQL
                .append(stringBuilderSQLValues);

        log.debug("sql insert = {}", stringBuilderSQL.toString());
        return executeUpdateForInsert(stringBuilderSQL.toString(), objectsValues);
    }






    // ------------ Dynamic update statement own map param (*** apply custom annotation) ------------
    public Integer updateOne(Class<T> aBeanClass, String uniqColumnName,Object ...params){

        StringBuilder stringBuilderSQL;

        stringBuilderSQL = new StringBuilder();

        stringBuilderSQL.append(SQLSyntaxCommon.UPDATE);
        stringBuilderSQL.append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        stringBuilderSQL.append(" set ");

        Field[] fields = aBeanClass.getDeclaredFields(); // get each props as array of class

        for (Field field : fields) {

            if (!field.isAnnotationPresent(IgnoreGenerateSQL.class)) {

                Column columnAnnotation = null;
                String fieldName;

                if (field.isAnnotationPresent(Column.class)) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                } else {
                    fieldName = field.getName();
                }

                if (!uniqColumnName.equals(fieldName)) {
                    stringBuilderSQL.append(fieldName)
                            .append(SQLSyntaxCommon.ASSIGN_EQUAL)
                            .append(" ,");
                }

            }

        } // end for

        stringBuilderSQL
                .deleteCharAt(stringBuilderSQL.length() - 1)
                .append(" where ")
                .append(uniqColumnName)
                .append(SQLSyntaxCommon.ASSIGN_EQUAL);

        log.debug("sql update = {}", stringBuilderSQL.toString());
        return executeUpdateForUpdate(stringBuilderSQL.toString(),params);
    }

    // ------------ Dynamic update statement auto map param (*** apply custom annotation) ------------
    public Integer updateOne(Class<T> aBeanClass, String uniqColumnName, T aBeanObject) throws IllegalAccessException {

        StringBuilder stringBuilderSQL;
        stringBuilderSQL = new StringBuilder();

        stringBuilderSQL.append(SQLSyntaxCommon.UPDATE);
        stringBuilderSQL.append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        stringBuilderSQL.append(" set ");

        Field[] fields = aBeanClass.getDeclaredFields(); // get each props as array of class
        List<Object> objectsValues = new ArrayList<>();

        for (Field field : fields) {

            if (!field.isAnnotationPresent(IgnoreGenerateSQL.class)) {

                field.setAccessible(true); // **** Make the field accessible if it's private
                Column columnAnnotation = null;
                String fieldName;
                Object fieldValue;

                if (field.isAnnotationPresent(Column.class)) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                } else {
                    fieldName = field.getName();
                }

                if (!uniqColumnName.equals(fieldName)) { // uniqColumnName.equals(fieldName) have to do on last element
                    stringBuilderSQL.append(fieldName)
                            .append(SQLSyntaxCommon.ASSIGN_EQUAL)
                            .append(" ,");

                    fieldValue = field.get(aBeanObject); // **** Get the value of the field for the specific POJO instance
                    log.debug("Field Name: {} , Value: {}", fieldName, fieldValue);
                    objectsValues.add(fieldValue);
                }

            }

        } // end for


        for (Field field : fields) {
            String fieldName;
            Column columnAnnotation = null;
            if (field.isAnnotationPresent(Column.class)) {
                columnAnnotation = field.getAnnotation(Column.class);
            }
            if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
            } else {
                fieldName = field.getName();
            }
            Object fieldValue;
            if (uniqColumnName.equals(fieldName)) {
                field.setAccessible(true); // **** Make the field accessible if it's private
                fieldValue = field.get(aBeanObject); // **** Get the value of the field for the specific POJO instance
                log.debug("Field Name: {} , Value: {}", fieldName, fieldValue);
                objectsValues.add(fieldValue);
            }
        }

        stringBuilderSQL
                .deleteCharAt(stringBuilderSQL.length() - 1)
                .append(" where ")
                .append(uniqColumnName)
                .append(SQLSyntaxCommon.ASSIGN_EQUAL);

        log.debug("sql update = {}", stringBuilderSQL.toString());
        return executeUpdateForUpdate(stringBuilderSQL.toString(),objectsValues);
    }

    // ------------ Dynamic update statement auto map param (*** apply custom annotation) *** have subclass  ------------
    public Integer updateOneIsPkSubclass(Class<T> aBeanClass, String uniqColumnName, T aBeanObject) throws IllegalAccessException {

        StringBuilder stringBuilderSQL;
        stringBuilderSQL = new StringBuilder();

        stringBuilderSQL.append(SQLSyntaxCommon.UPDATE);
        stringBuilderSQL.append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        stringBuilderSQL.append(" set ");

        List<Field> fields = new ArrayList<>();

        if (aBeanClass.getSuperclass() != null) { // check if have a subclass then get all fields
            Class<?> superclass = aBeanClass.getSuperclass();
            for (Field field : superclass.getDeclaredFields()) {
                fields.add(field);
            }
        }

        for (Field field : aBeanClass.getDeclaredFields()) { // get all field of main class
            fields.add(field);
        }
        // now field already works

        List<Object> objectsValues = new ArrayList<>();

        for (Field field : fields) {

            if (!field.isAnnotationPresent(IgnoreGenerateSQL.class)) {

                field.setAccessible(true); // **** Make the field accessible if it's private
                Column columnAnnotation = null;
                String fieldName;
                Object fieldValue;

                if (field.isAnnotationPresent(Column.class)) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                } else {
                    fieldName = field.getName();
                }

                if (!uniqColumnName.equals(fieldName)) { // uniqColumnName.equals(fieldName) have to do on last element
                    stringBuilderSQL.append(fieldName)
                            .append(SQLSyntaxCommon.ASSIGN_EQUAL)
                            .append(" ,");

                    fieldValue = field.get(aBeanObject); // **** Get the value of the field for the specific POJO instance
                    log.debug("Field Name: {} , Value: {}", fieldName, fieldValue);
                    objectsValues.add(fieldValue);
                }

            }

        } // end for


        for (Field field : fields) {
            String fieldName;
            Column columnAnnotation = null;
            if (field.isAnnotationPresent(Column.class)) {
                columnAnnotation = field.getAnnotation(Column.class);
            }
            if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                fieldName = columnAnnotation.value();  // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
            } else {
                fieldName = field.getName();
            }
            Object fieldValue;
            if (uniqColumnName.equals(fieldName)) {
                field.setAccessible(true); // **** Make the field accessible if it's private
                fieldValue = field.get(aBeanObject); // **** Get the value of the field for the specific POJO instance
                log.debug("Field Name: {} , Value: {}", fieldName, fieldValue);
                objectsValues.add(fieldValue);
            }
        }

        stringBuilderSQL
                .deleteCharAt(stringBuilderSQL.length() - 1)
                .append(" where ")
                .append(uniqColumnName)
                .append(SQLSyntaxCommon.ASSIGN_EQUAL);

        log.debug("sql update = {}", stringBuilderSQL.toString());
        return executeUpdateForUpdate(stringBuilderSQL.toString(), objectsValues);
    }






    // ------------ Dynamic  ------------
    public Integer deleteOne(Class<T> aBeanClass, String uniqColumnName,Object uniqValue) throws IllegalAccessException {
        StringBuilder stringBuilderSQL = new StringBuilder()
                .append(SQLSyntaxCommon.DELETE)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass))
                .append(" where ")
                .append(uniqColumnName+" ")
                .append(SQLSyntaxCommon.ASSIGN_EQUAL);
        log.debug("sql delete = {}", stringBuilderSQL.toString());
        return executeUpdateForDelete(stringBuilderSQL.toString(),uniqValue);
    }







    // ------------ execute with map params ------------
    public void executeStatement(String filename,HashMap<String,Object> params) {
        StringBuilder stringBuilderSQL = usefulGetSQLStatement.readSQLFileAsStatement(filename);
        replaceAssignValuesByHasMap(stringBuilderSQL,params); // concept no return
        log.debug("sql replace = {}", stringBuilderSQL.toString());
        executeQueryForStatement(stringBuilderSQL.toString());
    }

    // ------------ execute no params ------------
    public void executeStatement(String filename) {
        StringBuilder stringBuilderSQL = usefulGetSQLStatement.readSQLFileAsStatement(filename);
        executeQueryForStatement(stringBuilderSQL.toString());
    }

    // Helpers for execute***
    private static void replaceAssignValuesByHasMap(StringBuilder stringBuilderSQL, HashMap<String,Object> params) {
        String sql = stringBuilderSQL.toString();
        int paramCount = params.size();
        String[] keys = new String[paramCount];
        Object[] values = new Object[paramCount];
        int i = 0;
        for (String key : params.keySet()) {
            keys[i] = key;
            i++;
        }
        i=0;
        for (Object value : params.values()) {
            values[i] = value;
            i++;
        }
        for (i = 0; i < paramCount ; i++) {
            Object value = values[i];
            String key = keys[i];
            if (key.startsWith("[")) {
                if (value == null ) {
                    sql = sql.replace(key,"null");
                } else {
                    sql = sql.replace(key, value.toString());
                }
            }
            else {
                if ( value == null ) {
                    sql = sql.replace(key, "null"); // replace without ' '
                }
                if ( isNumeric(value) || isBool(value) ) {
                    sql = sql.replace(key, value.toString()); // replace without ' '
                }
                else {
                    sql = sql.replace(key, "'"+value+"'"); // replace with ' '
                }
            }

        }
        stringBuilderSQL.setLength(0); // way to clear string builder
        stringBuilderSQL.append(sql);
    }

    private static boolean isNumeric(Object str) {
        if (str == null || str.toString().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str.toString()); // Or Integer.parseInt(str) for integers
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isBool(Object str) {
        if (str == null || str.toString().isEmpty()) {
            return false;
        }
        try {
            return str instanceof Boolean;
        } catch (IllegalArgumentException | NullPointerException e ) {
            return false;
        }
    }




}

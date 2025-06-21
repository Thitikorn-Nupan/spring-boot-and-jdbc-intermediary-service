package com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete;

import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.JdbcExecuteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.annotation.IgnoreGenerateSQL;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_common.SQLSyntaxCommon;
import com.ttknp.springbootandjdbcintermediaryservice.services.useful.UsefulJdbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JdbcInsertUpdateDeleteHelper<T> extends JdbcExecuteHelper {

    private static final Logger log = LoggerFactory.getLogger(JdbcInsertUpdateDeleteHelper.class);
    private final UsefulJdbcService usefulJdbcService;

    @Autowired
    public JdbcInsertUpdateDeleteHelper(JdbcTemplate jdbcTemplate, UsefulJdbcService usefulJdbcService) {
        super(jdbcTemplate);
        this.usefulJdbcService = usefulJdbcService;
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

}

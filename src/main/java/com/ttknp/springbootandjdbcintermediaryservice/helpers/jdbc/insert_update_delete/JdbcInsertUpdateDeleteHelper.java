package com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.insert_update_delete;

import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.JdbcExecuteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_common.SQLSyntaxCommon;
import com.ttknp.springbootandjdbcintermediaryservice.services.useful.UsefulJdbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;

@Service
public class JdbcInsertUpdateDeleteHelper<T> extends JdbcExecuteHelper {

    private static final Logger log = LoggerFactory.getLogger(JdbcInsertUpdateDeleteHelper.class);
    private final UsefulJdbcService usefulJdbcService;

    @Autowired
    public JdbcInsertUpdateDeleteHelper(JdbcTemplate jdbcTemplate, UsefulJdbcService usefulJdbcService) {
        super(jdbcTemplate);
        this.usefulJdbcService = usefulJdbcService;
    }

    // ------------ Dynamic insert statement own map param ------------
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

            // log.debug("field {} is ignore {}" , field.getName() , foundIgnoreField);

            if (!foundIgnoreField) {

                // Note, Don't forget mark @Column("<real field name>") if it's not the same property name and column name
                Column columnAnnotation = field.getAnnotation(Column.class);
                String fieldName = field.getName();

                if (columnAnnotation != null) { // check if property name (POJO) it's not the same column name (Field Table)
                    // columnAnnotation => @org.springframework.data.relational.core.mapping.Column("full_name") columnAnnotation.value() =>  full_name
                    fieldName = columnAnnotation.value();
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

        log.debug("sql insert = {}", stringBuilderSQL.toString());
        return executeUpdateForInsert(stringBuilderSQL.toString(),params);
        // return 1;
    }

    // ------------ Auto generate insert statement and map params ------------
    public Integer insertOne(Class<T> aBeanClass,String autoGenerateColumnName,T object){
        String schema = usefulJdbcService.getSchemaNameOnTableAnnotation(aBeanClass);
        String tableName = usefulJdbcService.getTableNameOnTableAnnotation(aBeanClass);
        return executeSimpleInsertByBeanPropertySqlParameterSource(schema,tableName,autoGenerateColumnName,object);
    }

}

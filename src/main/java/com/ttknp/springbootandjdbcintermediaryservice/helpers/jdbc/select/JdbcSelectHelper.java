package com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.select;

import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_order_by.SqlOrderByHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.JdbcExecuteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_common.SQLSyntaxCommon;
import com.ttknp.springbootandjdbcintermediaryservice.services.useful.UsefulGetSQLStatement;
import com.ttknp.springbootandjdbcintermediaryservice.services.useful.UsefulJdbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class JdbcSelectHelper<T> extends JdbcExecuteHelper {

    private static final Logger log = LoggerFactory.getLogger(JdbcSelectHelper.class);
    private final UsefulJdbcService usefulJdbcService;
    private final UsefulGetSQLStatement usefulGetSQLStatement;

    @Autowired
    public JdbcSelectHelper(JdbcTemplate jdbcTemplate, UsefulJdbcService usefulJdbcService, UsefulGetSQLStatement usefulGetSQLStatement) {
        super(jdbcTemplate);
        this.usefulJdbcService = usefulJdbcService;
        this.usefulGetSQLStatement = usefulGetSQLStatement;
    }

    // ------------ Dynamic select statement ------------
    // ------------ Select as List ------------
    public List<Map<String, Object>> selectAllAsMap(Class<T> aBeanClass) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_START)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        return executeQueryForList(stringBuilder.toString());
    }

    public List<T> selectAll(Class<T> aBeanClass, SqlOrderByHelper<T> sqlOrderByHelper) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_START)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        log.debug("before stringBuilder thru implement = {}",stringBuilder.toString()); // First step => select * from h2_shop.customers
        if (sqlOrderByHelper != null) {
            String alias = SQLSyntaxCommon.ALIAS;
            stringBuilder.append(" as ")
                         .append(alias)
                         .append(" order by ");
            // Second step => select * from h2_shop.customers as alias order by
            /*
                Note!! appendOrderBy(...) works on CustomerController class or class that implement SqlOrderByHelper interface
                Ex, sqlOrderByHelper = ((stringBuilder, alias, model) -> {...}); on CustomerController.getAllCustomersOrderBy(...);
            */
            sqlOrderByHelper.appendOrderBy(stringBuilder, alias, null);
        }
        log.debug("after stringBuilder thru implement = {}",stringBuilder.toString()); // Last step => select * from h2_shop.customers ORDER BY FULL_NAME asc,...
        return executeQueryByBeanPropertyRowMapper(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    public List<T> selectAll(Class<T> aBeanClass,StringBuilder stringBuilderSql, SqlOrderByHelper<T> sqlOrderByHelper) {
        if (sqlOrderByHelper != null) {
            HashMap<String,Object> params = new HashMap<>();
            String alias = SQLSyntaxCommon.ALIAS;

            // create sql order by as dynamic
            StringBuilder stringBuilderOrderBy = new StringBuilder();
            stringBuilderOrderBy.append(" as ")
                    .append(alias)
                    .append(" order by ");
            // after thru implement = select * from h2_shop.customers as alias order by alias.level desc limit 100;
            sqlOrderByHelper.appendOrderBy(stringBuilderOrderBy, alias, null);

            // replace stringBuilderOrderBy as string to [SQL_CONDITION] on stringBuilderSql
            params.put("[SQL_CONDITION]", stringBuilderOrderBy.toString());
            replaceAssignValuesByHasMap(stringBuilderSql, params);
            // log.debug("stringBuilderSql after replace = {}", stringBuilderSql.toString()); // SELECT * FROM H2_SHOP.CUSTOMERS  as alias order by  alias.level desc limit 100;
        }
        return executeQueryByBeanPropertyRowMapper(stringBuilderSql.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    public List<T> selectAll(Class<T> aBeanClass) {
        StringBuilder stringBuilder = new StringBuilder()
                  .append(SQLSyntaxCommon.SELECT_START)
                  .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        return executeQueryByBeanPropertyRowMapper(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    public List<T> selectAll(String sql,Class<T> aBeanClass) {
        return executeQueryByBeanPropertyRowMapper(sql, new BeanPropertyRowMapper<T>(aBeanClass));
    }

    public List<T> selectAllWhereLikeAColumn(Class<T> aBeanClass, String columnName, Object value) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_START)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass))
                .append(" where "+columnName+" like ")
                .append(SQLSyntaxCommon.ASSIGN);
        value = "%"+value.toString()+"%";
        // log.debug("sql = {} , value = {}",stringBuilder.toString(),value); // sql = select * from H2_SCHOOL.STUDENTS where level like ? , value = %B%
        return executeQueryByBeanPropertyRowMapperParams(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass), value);
    }

    public <U> List<U> selectAllOnlyColumn(Class<T> aBeanClass, Class<U> aTypeClass ,String columnName) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("select ")
                .append(columnName)
                .append(" from ")
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        return executeQueryForListProperty(stringBuilder.toString(),aTypeClass);
    }

    public <U> List<U> selectAllOnlyColumnWhereLikeAColumn(Class<T> aBeanClass, Class<U> aTypeClass ,String columnName,String uniqColumName, Object uniqValue) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("select ")
                .append(columnName)
                .append(" from ")
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass))
                .append(" where "+uniqColumName+" like ")
                .append(SQLSyntaxCommon.ASSIGN);
        uniqValue = "%"+uniqValue.toString()+"%";
        log.debug("sql = {} , value = {}",stringBuilder.toString(),uniqValue);
        return executeQueryForListPropertyParams(stringBuilder.toString(),aTypeClass,uniqValue);
    }





    // ------------ Select as An Object ------------
    public T selectOne(Class<T> aBeanClass, String uniqColumnName, Object uniqValue) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_START)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass))
                .append(" where ")
                .append(uniqColumnName+" ")
                .append(SQLSyntaxCommon.ASSIGN_EQUAL);
        return executeQueryForObjectByBeanPropertyRowMapperParams(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass), uniqValue);
    }

    public T selectOne(String sql , Class<T> aBeanClass, Object uniqValue) {
        return executeQueryForObjectByBeanPropertyRowMapperParams(sql, new BeanPropertyRowMapper<T>(aBeanClass), uniqValue);
    }

    public T selectOne(String sql , Class<T> aBeanClass) {
        return executeQueryForObjectByBeanPropertyRowMapper(sql, new BeanPropertyRowMapper<T>(aBeanClass));
    }






    // ------------ Select as An Property ------------
    public <U> U selectOneOnlyColumn(Class<T> aBeanClass, Class<U> aTypeClass ,String columnName,String uniqColumName, Object uniqValue) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("select ")
                .append(columnName)
                .append(" from ")
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass))
                .append(" where "+uniqColumName)
                .append(SQLSyntaxCommon.ASSIGN_EQUAL);
        return executeQueryForObjectPropertyParamsNoMapping(stringBuilder.toString(),aTypeClass,uniqValue);
    }

    public Integer selectCount( Class<T> aBeanClass , String uniqColumnName, Object uniqValue) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_COUNT)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass))
                .append(" where ")
                .append(uniqColumnName)
                .append(SQLSyntaxCommon.ASSIGN_EQUAL);
        return executeQueryForObjectPropertyParamsNoMapping(stringBuilder.toString(),Integer.class,uniqValue);
    }

    public Integer selectCount( Class<T> aBeanClass ) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_COUNT)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        return executeQueryForObjectPropertyNoMapping(stringBuilder.toString(),Integer.class);
    }






    // ------------ Select as List Or Object  ------------
    public <U> U selectBoth(Class<T> aBeanClass, ResultSetExtractor<U> resultSetExtractor) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_START)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        return executeResultSetExtractor(stringBuilder.toString(), resultSetExtractor);
    }

    public <U> U selectBothWhereLikeAColumn(Class<T> aBeanClass, ResultSetExtractor<U> resultSetExtractor,String columnName, Object value) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_START)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass))
                .append(" where "+columnName+" like ")
                .append(SQLSyntaxCommon.ASSIGN);
        value = "%"+value.toString()+"%";
        return executeResultSetExtractorParams(stringBuilder.toString(), resultSetExtractor, value);
    }


    // ------------ get statement ------------
    public StringBuilder getStatement(String filename) {
        StringBuilder stringBuilderSQL = usefulGetSQLStatement.readSQLFileAsStatement(filename);
        return stringBuilderSQL;
    }

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

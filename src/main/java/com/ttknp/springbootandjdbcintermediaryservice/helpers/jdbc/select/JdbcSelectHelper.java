package com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.select;

import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.JdbcExecuteHelper;
import com.ttknp.springbootandjdbcintermediaryservice.helpers.sql_common.SQLSyntaxCommon;
import com.ttknp.springbootandjdbcintermediaryservice.services.useful.UsefulJdbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JdbcSelectHelper<T> extends JdbcExecuteHelper {

    private static final Logger log = LoggerFactory.getLogger(JdbcSelectHelper.class);
    private final UsefulJdbcService usefulJdbcService;

    @Autowired
    public JdbcSelectHelper(JdbcTemplate jdbcTemplate, UsefulJdbcService usefulJdbcService) {
        super(jdbcTemplate);
        this.usefulJdbcService = usefulJdbcService;
    }



    // ------------ Dynamic select statement ------------
    // ------------ Select as List ------------
    public List<Map<String, Object>> selectAllAsMap(Class<T> aBeanClass) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntaxCommon.SELECT_START)
                .append(usefulJdbcService.getSchemaAndTableNameOnTableAnnotation(aBeanClass));
        return executeQueryForList(stringBuilder.toString());
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

}

package com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school;



import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.annotation.IgnoreGenerateSQL;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.repository.Lock;

import java.util.Date;


// @Table(..) No need if you don't use @Repository on CrudRepository, But if you want to get table/schema name you can mark it
@Table(name = "students",schema = "h2_school")
public class Student {

    @IgnoreGenerateSQL
    private Long sid;
    @Column("full_name")
    private String fullName;
    private Date birthday;
    private String level;
    @IgnoreGenerateSQL
    private String teacherName;

    public Student() {}

    public Student(Long sid, String fullName, Date birthday, String level) {
        this.sid = sid;
        this.fullName = fullName;
        this.birthday = birthday;
        this.level = level;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Student{");
        sb.append("sid=").append(sid);
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", birthday=").append(birthday);
        sb.append(", level=").append(level);
        sb.append('}');
        return sb.toString();
    }
}

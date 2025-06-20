package com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school;



import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;


// @Table(..) No need if you don't use @Repository on CrudRepository, But if you want to get table/schema name you can mark it
@Table(name = "teachers",schema = "h2_school")
public class Teacher {

    private Long tid;
    @Column("full_name")
    private String fullName;
    private Date birthday;
    @Column("class_id")
    private String classId;

    public Teacher() {}

    public Teacher(Long tid, String fullName, Date birthday, String classId) {
        this.tid = tid;
        this.fullName = fullName;
        this.birthday = birthday;
        this.classId = classId;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Teacher{");
        sb.append("tid=").append(tid);
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", birthday=").append(birthday);
        sb.append(", classId='").append(classId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

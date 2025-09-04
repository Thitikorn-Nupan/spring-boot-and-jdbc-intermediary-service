package com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop;



import com.ttknp.springbootandjdbcintermediaryservice.entities.h2_shop.pk.PKUUID;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
import java.util.UUID;


// @Table(..) No need if you don't use @Repository on CrudRepository, But if you want to get table/schema name you can mark it
@Table(name = "employees",schema = "h2_shop")
public class Employee extends PKUUID {

    @Column("full_name")
    private String fullName;
    private Date birthday;
    private String level;


    public Employee() {
        super(null);
    }

    public Employee( String fullName, Date birthday, String level) {
        super(UUID.randomUUID().toString());
        this.fullName = fullName;
        this.birthday = birthday;
        this.level = level;
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
        sb.append("eid=").append(this.getId());
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", birthday=").append(birthday);
        sb.append(", level=").append(level);
        sb.append('}');
        return sb.toString();
    }
}

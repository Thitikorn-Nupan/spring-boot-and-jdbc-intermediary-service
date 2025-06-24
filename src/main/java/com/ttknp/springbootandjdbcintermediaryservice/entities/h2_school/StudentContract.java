package com.ttknp.springbootandjdbcintermediaryservice.entities.h2_school;

import com.ttknp.springbootandjdbcintermediaryservice.helpers.jdbc.annotation.IgnoreGenerateSQL;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

// @Table(..) No need if you don't use @Repository on CrudRepository, But if you want to get table/schema name you can mark it
@Table(name = "students_contract",schema = "h2_school")
public class StudentContract {
    @IgnoreGenerateSQL
    private Long cid;
    private Long sid;
    @Column("contract_type")
    private String contractType;
    @Column("contract_start_date")
    private Date contractStartDate;
    @Column("contract_end_date")
    private Date contractEndDate;
    @Column("parent_guardian_name")
    private String parentGuardianName;
    @Column("parent_guardian_phone")
    private String parentGuardianPhone;
    @Column("parent_guardian_address")
    private String parentGuardianAddress;
    @Column("emergency_contact_name")
    private String emergencyContactName;
    @Column("emergency_contact_phone")
    private String emergencyContactPhone;
    @Column("contract_status")
    private Boolean contractStatus;

    public StudentContract() {
    }

    public StudentContract(Boolean contractStatus, String emergencyContactPhone, String emergencyContactName, String parentGuardianAddress, String parentGuardianPhone, String parentGuardianName, Date contractEndDate, Date contractStartDate, String contractType, Long sid, Long cid) {
        this.contractStatus = contractStatus;
        this.emergencyContactPhone = emergencyContactPhone;
        this.emergencyContactName = emergencyContactName;
        this.parentGuardianAddress = parentGuardianAddress;
        this.parentGuardianPhone = parentGuardianPhone;
        this.parentGuardianName = parentGuardianName;
        this.contractEndDate = contractEndDate;
        this.contractStartDate = contractStartDate;
        this.contractType = contractType;
        this.sid = sid;
        this.cid = cid;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getParentGuardianName() {
        return parentGuardianName;
    }

    public void setParentGuardianName(String parentGuardianName) {
        this.parentGuardianName = parentGuardianName;
    }

    public String getParentGuardianPhone() {
        return parentGuardianPhone;
    }

    public void setParentGuardianPhone(String parentGuardianPhone) {
        this.parentGuardianPhone = parentGuardianPhone;
    }

    public String getParentGuardianAddress() {
        return parentGuardianAddress;
    }

    public void setParentGuardianAddress(String parentGuardianAddress) {
        this.parentGuardianAddress = parentGuardianAddress;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public Boolean getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Boolean contractStatus) {
        this.contractStatus = contractStatus;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("StudentContract{");
        sb.append("cid=").append(cid);
        sb.append(", sid=").append(sid);
        sb.append(", contractType='").append(contractType).append('\'');
        sb.append(", contractStartDate=").append(contractStartDate);
        sb.append(", contractEndDate=").append(contractEndDate);
        sb.append(", parentGuardianName='").append(parentGuardianName).append('\'');
        sb.append(", parentGuardianPhone='").append(parentGuardianPhone).append('\'');
        sb.append(", parentGuardianAddress='").append(parentGuardianAddress).append('\'');
        sb.append(", emergencyContactName='").append(emergencyContactName).append('\'');
        sb.append(", emergencyContactPhone='").append(emergencyContactPhone).append('\'');
        sb.append(", contractStatus=").append(contractStatus);
        sb.append('}');
        return sb.toString();
    }
}

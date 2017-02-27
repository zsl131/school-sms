package com.zslin.web.model;

import com.zslin.sms.annotations.TableHead;
import com.zslin.web.vo.BaseEntity;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 11:24.
 */
@Entity
@Table(name = "t_student")
@TableHead(name = "学生")
public class Student extends BaseEntity {

    @TableHead(name = "学生姓名")
    private String name;

    @TableHead(name = "学生身份证号")
    private String identity;

    @TableHead(name = "学生手机号码")
    private String phone;

    /** 学号 */
    @Column(name = "stu_no")
    @TableHead(name = "学生学号")
    private String stuNo;

    /** 所在学校Id */
    @Column(name = "sch_id")
    private Integer schId;

    /** 所在学校名称 */
    @Column(name = "sch_name")
    @TableHead(name = "所在学校")
    private String schName;

    /** 所在班级Id */
    @Column(name = "cla_id")
    private Integer claId;

    /** 所在班级名称 */
    @Column(name = "cla_name")
    @TableHead(name = "所在班级")
    private String claName;

    /** 0-毕业；-1：删除；1-正常 */
    private String status;

    /** 家庭地址 */
    @TableHead(name = "家庭住址")
    private String address;

    @Column(name = "father_name")
    @TableHead(name = "父亲姓名")
    private String fatherName;

    @Column(name = "father_phone")
    @TableHead(name = "父亲手机号码")
    private String fatherPhone;

    @Column(name = "mother_name")
    @TableHead(name = "母亲姓名")
    private String motherName;

    @Column(name = "mother_phone")
    @TableHead(name = "母亲手机号码")
    private String motherPhone;

    /** 性别 */
    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherPhone() {
        return fatherPhone;
    }

    public void setFatherPhone(String fatherPhone) {
        this.fatherPhone = fatherPhone;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherPhone() {
        return motherPhone;
    }

    public void setMotherPhone(String motherPhone) {
        this.motherPhone = motherPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public Integer getSchId() {
        return schId;
    }

    public void setSchId(Integer schId) {
        this.schId = schId;
    }

    public String getSchName() {
        return schName;
    }

    public void setSchName(String schName) {
        this.schName = schName;
    }

    public Integer getClaId() {
        return claId;
    }

    public void setClaId(Integer claId) {
        this.claId = claId;
    }

    public String getClaName() {
        return claName;
    }

    public void setClaName(String claName) {
        this.claName = claName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

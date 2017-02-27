package com.zslin.web.model;

import com.zslin.sms.annotations.TableHead;
import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 11:12.
 */
@Entity
@Table(name = "t_teacher")
@TableHead(name = "老师")
public class Teacher extends BaseEntity {

    @TableHead(name = "姓名")
    private String name;

    private String sex;

    @TableHead(name = "电话号码")
    private String phone;

    private String openid;

    @Column(name = "account_id")
    private Integer accountId;

    private String email;

    @TableHead(name = "身份证号")
    private String identity;

    @Column(name = "sch_id")
    private Integer schId;

    @Column(name = "sch_name")
    @TableHead(name = "所在学校")
    private String schName;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}

package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 11:11.
 */
@Entity
@Table(name = "t_classes")
public class Classes extends BaseEntity {

    /** 所在学校Id */
    @Column(name = "sch_id")
    private Integer schId;

    /** 所在学校名称 */
    @Column(name = "sch_name")
    private String schName;

    private String name;

    /** 班主任ID */
    @Column(name = "master_id")
    private Integer masterId;

    /** 班主任姓名 */
    @Column(name = "master_name")
    private String masterName;

    /** 班主任Openid */
    @Column(name = "master_openid")
    private String masterOpenid;

    /** 班主任联系电话 */
    @Column(name = "master_phone")
    private String masterPhone;

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

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getMasterOpenid() {
        return masterOpenid;
    }

    public void setMasterOpenid(String masterOpenid) {
        this.masterOpenid = masterOpenid;
    }

    public String getMasterPhone() {
        return masterPhone;
    }

    public void setMasterPhone(String masterPhone) {
        this.masterPhone = masterPhone;
    }
}

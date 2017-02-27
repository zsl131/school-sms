package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:31.
 */
@Entity
@Table(name = "t_account")
public class Account extends BaseEntity {

    private String nickname;

    private String name;

    private String headimgurl;

    private String sex;

    private String phone;

    private String identity;

    /** 状态：0-取消关注；1-关注中 */
    private String status;

    /**
     * 类型
     * 0-普通用户；
     * 1-学生；
     * 2-科任老师；
     * 5-班主任；
     * 6-学生家长；
     * 10-超级管理人员
     */
    private String type;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    private String openid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.zslin.wx.tools;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 10:36.
 */
@Component
@ConfigurationProperties(locations = "classpath:wx.properties")
public class WxConfig {
    private String appid;
    private String secret;
    private String token;
    private String aeskey;
    private String eventTemp;

    public String getEventTemp() {
        return eventTemp;
    }

    public void setEventTemp(String eventTemp) {
        this.eventTemp = eventTemp;
    }

    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package com.zslin.sms.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/19 10:21.
 * 群发短信时从页面传入参数的DTO对象
 */
public class ParamDto {
    //phone对应的字段
    private String phone;

    //除了phone以外的其他对应关系
    private Map<String, String> maps;

    public ParamDto() {
        if(maps == null) {
            maps = new HashMap<>();
        }
    }

    public void put(String key, String value) {
        this.maps.put(key, value);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<String, String> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, String> maps) {
        this.maps = maps;
    }
}

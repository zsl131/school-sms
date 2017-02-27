package com.zslin.sms.tools;

import com.zslin.sms.dto.ParamDto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/19 10:24.
 * 群发短信时处理页面传入参数的工具类
 */
public class ParamTools {

    public static ParamDto buildParams(String pars) {
        ParamDto dto = new ParamDto();
        String [] par_array = pars.split("&");
        for(String par : par_array) {
            if(par==null || "".equals(par) || par.indexOf("=")<0) {continue;}
            String [] kv = par.split("=");
            String key = kv[0];
            String value = kv[1];
            if("phone".equals(key)) {dto.setPhone(value);}
            else {
                dto.put(key.replace("#", ""), value);
            }
        }
        return dto;
    }
}

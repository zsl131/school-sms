package com.zslin.wx.tools;

import com.zslin.cache.CacheTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 11:05.
 */
@Component
public class AccessTokenTools {

    private static final String NAME = "wx-access-token";
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    @Autowired
    private CacheTools cacheTools;
    @Autowired
    private WxConfig wxConfig;

    public String getAccessToken() {
        String token = (String) cacheTools.getKey(NAME);
        if(token==null) {
            token = getNewAccessToken();
            cacheTools.putKey(NAME, token, 7000);
        }
        return token;
    }

    private String getNewAccessToken() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("grant_type", "client_credential");
        params.put("appid", wxConfig.getAppid());
        params.put("secret", wxConfig.getSecret());
        String result = InternetTools.doGet(TOKEN_URL, params);
        return JsonTools.getJsonParam(result, "access_token");
    }
}

package com.zslin.timer.service;

import com.zslin.sms.dto.ParamDto;
import com.zslin.sms.tools.DatabaseTools;
import com.zslin.sms.tools.ExcelCacheTools;
import com.zslin.sms.tools.ParamTools;
import com.zslin.sms.tools.SmsTools;
import com.zslin.web.service.IMyTaskService;
import com.zslin.wx.tools.JsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/23 11:46.
 * 批量发送短信的定时任务
 */
@Component("sendSmsService")
public class SendSmsService {

    @Autowired
    private DatabaseTools databaseTools;

    @Autowired
    private ExcelCacheTools excelCacheTools;

    @Autowired
    private SmsTools smsTools;

    @Autowired
    private IMyTaskService myTaskService;

    public void sendSms(String json) {
        Integer mid = Integer.parseInt(JsonTools.getJsonParam(json, "mid"));
        String sourceType = JsonTools.getJsonParam(json, "sourceType");
        String token = JsonTools.getJsonParam(json, "token");
        String pars = JsonTools.getJsonParam(json, "pars");
        Integer tid = Integer.parseInt(JsonTools.getJsonParam(json, "tid"));
        sendSms(mid, sourceType, token, pars, tid);
    }

    private void sendSms(Integer mid, String sourceType, String token, String pars, Integer tid) {
        try {
            ParamDto paramDto = ParamTools.buildParams(pars);
            if("2".equals(sourceType)) { //从数据库中取数据
                List dataList = databaseTools.queryDatasByPackageName(token); //获取数据对象列表
                for(Object obj : dataList) {
                    String phone = databaseTools.buildValueByPackageName(obj, paramDto.getPhone()).toString(); //电话
                    smsTools.sendMsg(mid, phone, buildDatabaseParams(obj, paramDto.getMaps()));
                }
            } else if("1".equals(sourceType)) { //从Excel中导入数据群发短信
                //                ExcelDto ed = excelCacheTools.queryFromCache(token);
                List<Map<String, String>> dataList = excelCacheTools.buildDataRows(token);
                for(Map<String, String> map : dataList) {
                    String phone = map.get(paramDto.getPhone()); //电话
                    smsTools.sendMsg(mid, phone, buildExcelParam(map, paramDto.getMaps()));
                }
            }
            if(tid>0) {
                myTaskService.updateStatus(tid, "1"); //修改为正在运行
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String [] buildExcelParam(Map<String, String> cellMap, Map<String, String> params) {
        String [] result = new String[params.size()*2];
        int i=0;
        for(String key : params.keySet()) {
            result[i] = key;
            result[i+1] = cellMap.get(params.get(key));
            i+=2;
        }
        return result;
    }

    private String [] buildDatabaseParams(Object obj, Map<String, String> params) throws Exception {
        String [] result = new String[params.size()*2];
        int i=0;
        for(String key : params.keySet()) {
            result[i] = key;
            result[i+1] = databaseTools.buildValueByPackageName(obj,params.get(key)).toString();
            i+=2;
        }
        return result;
    }
}

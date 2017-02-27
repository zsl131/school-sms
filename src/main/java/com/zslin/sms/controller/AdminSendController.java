package com.zslin.sms.controller;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.DateTools;
import com.zslin.sms.dto.DataDto;
import com.zslin.sms.dto.ExcelDto;
import com.zslin.sms.service.IModuleService;
import com.zslin.sms.tools.AnnotationTools;
import com.zslin.sms.tools.ExcelCacheTools;
import com.zslin.sms.tools.SmsTools;
import com.zslin.timer.service.SendSmsService;
import com.zslin.timer.tools.MyScheduleService;
import com.zslin.web.model.MyTask;
import com.zslin.web.service.IMyTaskService;
import com.zslin.web.tools.UploadResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 16:34.
 */
@Controller
@RequestMapping(value = "admin/send")
@AdminAuth(name = "短信发送管理", psn = "短信管理", orderNum = 2, porderNum = 1, pentity = 0, icon = "fa fa-envelope")
public class AdminSendController {

    @Autowired
    private SmsTools smsTools;

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private ExcelCacheTools excelCacheTools;

    @Autowired
    private AnnotationTools annotationTools;

//    @Autowired
//    private DatabaseTools databaseTools;

    @Autowired
    private MyScheduleService myScheduleService;

    @Autowired
    private IMyTaskService myTaskService;

    @Autowired
    private SendSmsService sendSmsService;

    @GetMapping(value = "index")
    @AdminAuth(name = "群发短信", orderNum = 1, type = "1", icon = "fa fa-envelope")
    public String index(Model model) {
        Integer amount = smsTools.surplus(); //获取短信余额
        model.addAttribute("surplus", amount);

        model.addAttribute("moduleList", moduleService.findCanUse()); //获取所有可以使用的短信模板
        return "admin/send/index";
    }

    @AdminAuth(name="上传群发短信Excel文件", orderNum=6, icon = "fa fa-plus")
    @RequestMapping(value="uploadExcel", method= RequestMethod.POST)
    public @ResponseBody
    UploadResDto uploadExcel(HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        try {
            ExcelDto dto = null;
            if(files.length>=1) {
                dto = excelCacheTools.uploadExcel(files[0].getInputStream(), files[0].getOriginalFilename(), true);
                request.getSession().setAttribute("file-token", dto.getToken()); //将token存入Session中
            } else {
                dto = new ExcelDto();
            }
            return new UploadResDto(UploadResDto.SUC, JSON.toJSONString(dto));
        } catch (Exception e) {
//            throw new SystemException("文件解析出错");
            return new UploadResDto(UploadResDto.ERROR, e.getMessage());
        }
    }

    @AdminAuth(name="获取所有可用数据源", orderNum=7, icon = "fa fa-plus")
    @RequestMapping(value="queryAllSource", method= RequestMethod.POST)
    public @ResponseBody UploadResDto queryAllSource(HttpServletRequest request) {
        try {
            List<DataDto> dtoList = annotationTools.buildTableHeads();
            return new UploadResDto(UploadResDto.SUC, JSON.toJSONString(dtoList));
        } catch (Exception e) {
//            e.printStackTrace();
            return new UploadResDto(UploadResDto.ERROR, e.getMessage());
        }
    }

    @AdminAuth(name="获取数据源属性", orderNum=7, icon = "fa fa-plus")
    @RequestMapping(value="querySourcePars", method= RequestMethod.POST)
    public @ResponseBody UploadResDto querySourcePars(String packageName, HttpServletRequest request) {
        try {
            List<DataDto> dtoList = annotationTools.buildAttr(packageName);
            return new UploadResDto(UploadResDto.SUC, JSON.toJSONString(dtoList));
        } catch (Exception e) {
//            e.printStackTrace();
            return new UploadResDto(UploadResDto.ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "sendSms", method = RequestMethod.POST)
    @AdminAuth(name = "批量提交发送短信请求", orderNum = 8)
    public @ResponseBody String sendSms(HttpServletRequest request) {
        try {
            String sourceType = request.getParameter("sourceType"); //1-从Excel导入；2-从数据库选择
            String token = request.getParameter("token"); //sourceType为1时，token为文件md5值，sourceType为2时，token为实体类包名
            String pars = request.getParameter("pars"); //模板参数
            Integer mid = Integer.parseInt(request.getParameter("mid")); //模板Id
            String task = request.getParameter("task"); //是否启用定时器功能
            String time = request.getParameter("time"); //指定发送时间

//            System.out.println("mid:"+mid+",sourceType:"+sourceType+",token:"+token+",pars:"+pars);

            sendSms(task, time, mid, sourceType, token, pars);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "1";
    }

    private void sendSms(String task, String time, Integer mid, String sourceType, String token, String pars) throws Exception {
        if("1".equalsIgnoreCase(task)) { //如果定时则加入队列
            MyTask mt = new MyTask();
            mt.setRemark("发送时间："+time);
            mt.setPackageName("sendSmsService");
            mt.setFirstTime(time);
            mt.setMethodName("sendSms");
            mt.setName("定时发送批量短信");
            mt.setProcessType("0");
            mt.setStatus("0");
            mt.setTimes(1);
            mt.setParams(buildPars(mid, sourceType, token, pars, 0));
            myTaskService.save(mt); //先保存获取id
            Integer taskId = mt.getId();
            mt.setParams(buildPars(mid, sourceType, token, pars, taskId));
            myTaskService.save(mt);

            if("1".equalsIgnoreCase(sourceType)) {
                Date sendDate = DateTools.str2Date(time, "yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date();
                Integer timeout = ((int)(sendDate.before(curDate)?0:sendDate.getTime()-curDate.getTime())/1000) + 3600;
                excelCacheTools.resaveCache(token, timeout);
            }
            myScheduleService.addOneTimeTask(mt.getId()+"", new Runnable() {
                @Override
                public void run() {
//                    sendSms(mid, sourceType, token, pars);
                    sendSmsService.sendSms(buildPars(mid, sourceType, token, pars, taskId));

                }
            }, time, false, "发送时间："+time);
        } else {
//            sendSms(mid, sourceType, token, pars);
            sendSmsService.sendSms(buildPars(mid, sourceType, token, pars, 0));
        }
    }

    private String buildPars(Integer mid, String sourceType, String token, String pars, Integer taskId) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"mid\":").append(mid).append(",")
                .append("\"sourceType\":\"").append(sourceType).append("\",")
                .append("\"token\":\"").append(token).append("\",")
                .append("\"tid\":").append(taskId).append(",")
                .append("\"pars\":\"").append(pars).append("\"").append("}");
        return sb.toString();
    }

    /*private void sendSms(Integer mid, String sourceType, String token, String pars) {
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
    }*/
}

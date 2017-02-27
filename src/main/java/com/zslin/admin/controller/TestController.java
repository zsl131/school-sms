package com.zslin.admin.controller;

import com.zslin.timer.tools.MyScheduleService;
import com.zslin.timer.tools.ScheduledDto;
import com.zslin.timer.tools.ScheduledDtoTools;
import com.zslin.web.service.IMyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/20 14:59.
 */
@RestController
@RequestMapping(value = "test")
public class TestController {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private MyScheduleService myScheduleService;

    @Autowired
    private IMyTaskService myTaskService;

    @GetMapping(value = "testTimer")
    public String testTimer(String s) {
        String cron = "0/"+s+" * * * * ?";
//        myScheduleService.setCron(cron);
        return "1";
    }

    @GetMapping(value = "cancel")
    public String cancle(String name) {
        myScheduleService.cancel(name);
        return "1";
    }

    @GetMapping(value = "list")
    public List<ScheduledDto> list() {
        return ScheduledDtoTools.getInstance().getDtoList();
    }

    @GetMapping(value = "one")
    public String one(String name, String date, String remark) throws ParseException {
        myScheduleService.addOneTimeTask(name, new Runnable() {
            @Override
            public void run() {
                System.out.println(String.format("new one time task, name is %s, remark is %s，curDate is %s.", name, remark, dateFormat.format(new Date())));
            }
        }, "2017-02-22 "+date, true, remark);
        return "1";
    }

    @GetMapping(value = "addCron")
    public String addCron(String name, String s, String remark) {
        myScheduleService.addCronTask(name, new Runnable() {
            @Override
            public void run() {
                System.out.println(String.format("new cron task, name is %s, remark is %s，curDate is %s.", name, remark, dateFormat.format(new Date())));
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                String cron = "0/"+s+" * * * * ?";
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        }, true, remark);
        return "1";
    }
}

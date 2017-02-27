package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.sms.service.IModuleService;
import com.zslin.timer.tools.ScheduledDtoTools;
import com.zslin.web.service.IMyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/22 15:57.
 * 短信定时任务管理Controller
 */
@Controller
@RequestMapping(value = "admin/smsTask")
@AdminAuth(name = "短信定时任务", psn = "任务管理器", orderNum = 1, porderNum = 1, pentity = 0, icon = "fa fa-clock-o")
public class AdminSmsTaskController {

    @Autowired
    private IMyTaskService myTaskService;

    @GetMapping(value = "list")
    @AdminAuth(name = "短信定时任务列表", orderNum = 1, type = "1", icon = "fa fa-clock-o")
    public String list(Model model, HttpServletRequest request) {
        model.addAttribute("datas", ScheduledDtoTools.getInstance().getDtoList());
        return "admin/smsTask/list";
    }

    @AdminAuth(name="删除短信定时任务", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method= RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable String id) {
        try {
            ScheduledDtoTools.getInstance().remove(id);
            myTaskService.updateStatus(Integer.parseInt(id), "-1");
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}

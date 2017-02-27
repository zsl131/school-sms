package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.School;
import com.zslin.web.model.Teacher;
import com.zslin.web.service.IClassesService;
import com.zslin.web.service.ISchoolService;
import com.zslin.web.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 15:26.
 */
@Controller
@RequestMapping(value = "admin/teacher")
@AdminAuth(name = "老师管理", psn = "应用管理", orderNum = 5, porderNum = 1, pentity = 0, icon = "fa fa-address-book")
public class AdminTeacherController {

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private IClassesService classesService;

    @Autowired
    private ISchoolService schoolService;

    @GetMapping(value = "list")
    @AdminAuth(name = "老师管理", orderNum = 1, type = "1", icon = "fa fa-address-book")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Teacher> datas = teacherService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "admin/teacher/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加老师", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, Integer schId, HttpServletRequest request) {
        School s = schoolService.findOne(schId);
        model.addAttribute("school", s);
        model.addAttribute("teacher", new Teacher());
        return "admin/teacher/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Integer schId, Teacher teacher, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            School s = schoolService.findOne(schId);
            teacher.setSchId(s.getId());
            teacher.setSchName(s.getName());
            teacher.setCreateDate(new Date());
            teacher.setCreateLong(System.currentTimeMillis());
            teacher.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            teacher.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            teacherService.save(teacher);
        }
        return "redirect:/admin/teacher/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改老师", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Teacher t = teacherService.findOne(id);
        model.addAttribute("teacher", t);
        return "admin/teacher/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Teacher teacher, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Teacher t = teacherService.findOne(id);
            t.setName(teacher.getName());
            t.setEmail(teacher.getEmail());
            t.setIdentity(teacher.getIdentity());
            t.setPhone(teacher.getPhone());
            t.setSex(teacher.getSex());
            teacherService.save(t);
        }
        return "redirect:/admin/teacher/list";
    }

    @AdminAuth(name="删除老师", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            teacherService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}

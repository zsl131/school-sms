package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.service.IUserService;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Account;
import com.zslin.web.model.School;
import com.zslin.web.service.ISchoolService;
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
@RequestMapping(value = "admin/school")
@AdminAuth(name = "学校管理", psn = "应用管理", orderNum = 1, porderNum = 1, pentity = 0, icon = "fa fa-university")
public class AdminSchoolController {

    @Autowired
    private ISchoolService schoolService;

    @Autowired
    private IUserService userService;

    @GetMapping(value = "list")
    @AdminAuth(name = "学校管理", orderNum = 1, type = "1", icon = "fa fa-university")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<School> datas = schoolService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "admin/school/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加学校", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        School s = new School();
        model.addAttribute("school", s);
        model.addAttribute("userList", userService.findAll());
        return "admin/school/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, School school, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            school.setCreateDate(new Date());
            school.setCreateLong(System.currentTimeMillis());
            school.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            school.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            schoolService.save(school);
        }
        return "redirect:/admin/school/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改学校", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        School s = schoolService.findOne(id);
        model.addAttribute("userList", userService.findAll());
        model.addAttribute("school", s);
        return "admin/school/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, School school, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            School s = schoolService.findOne(id);
            s.setPhone(school.getPhone());
            s.setAddress(school.getAddress());
            s.setContact(school.getContact());
            s.setName(school.getName());
            s.setUserId(school.getUserId());
            s.setUsername(school.getUsername());
            schoolService.save(s);
        }
        return "redirect:/admin/school/list";
    }

    @AdminAuth(name="删除学校", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            schoolService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}

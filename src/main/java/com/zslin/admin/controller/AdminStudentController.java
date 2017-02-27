package com.zslin.admin.controller;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Classes;
import com.zslin.web.model.Student;
import com.zslin.web.service.IClassesService;
import com.zslin.web.service.IStudentService;
import com.zslin.web.tools.BatchOperateStuTools;
import com.zslin.web.tools.ExcelResultDto;
import com.zslin.web.tools.UploadResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 15:26.
 */
@Controller
@RequestMapping(value = "admin/student")
@AdminAuth(name = "学生管理", psn = "应用管理", orderNum = 1, porderNum = 1, pentity = 0, icon = "fa fa-mortar-board")
public class AdminStudentController {

    @Autowired
    private IStudentService studentService;

    @Autowired
    private IClassesService classesService;

    @Autowired
    private BatchOperateStuTools batchOperateStuTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "学生管理", orderNum = 1, type = "1", icon = "fa fa-mortar-board")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Student> datas = studentService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/student/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加学生", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, Integer claId, HttpServletRequest request) {
        Classes c = classesService.findOne(claId);
        model.addAttribute("classes", c);
        model.addAttribute("student", new Student());
        return "admin/student/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Integer claId, Student student, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            Classes c = classesService.findOne(claId);
            student.setClaId(c.getId());
            student.setClaName(c.getName());
            student.setSchId(c.getSchId());
            student.setSchName(c.getSchName());
            student.setCreateDate(new Date());
            student.setCreateLong(System.currentTimeMillis());
            student.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            student.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            studentService.save(student);
        }
        return "redirect:/admin/student/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改学生", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Student s = studentService.findOne(id);
        model.addAttribute("student", s);
        return "admin/student/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Student student, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Student s = studentService.findOne(id);
            MyBeanUtils.copyProperties(student, s, new String[]{"id", "createDate", "createDay", "createTime", "createLong"});
            studentService.save(s);
        }
        return "redirect:/admin/student/list";
    }

    @AdminAuth(name="删除学生", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            studentService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name="批量导入学生", orderNum=6, icon = "fa fa-plus")
    @RequestMapping(value="importStu", method=RequestMethod.POST)
    public @ResponseBody
    UploadResDto importStu(Integer claId, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        try {
            ExcelResultDto dto = null;
            if(files.length<=1) {
                dto = batchOperateStuTools.batchAddStu(files[0].getInputStream(), claId);
            } else {
                dto = new ExcelResultDto(0, 0, 0, "");
            }
            return new UploadResDto(UploadResDto.SUC, JSON.toJSONString(dto));
        } catch (Exception e) {
//            throw new SystemException("文件解析出错");
            return new UploadResDto(UploadResDto.ERROR, e.getMessage());
        }
    }

    @AdminAuth(name="批量删除学生", orderNum=6, icon = "fa fa-remove")
    @RequestMapping(value="removeStu", method=RequestMethod.POST)
    public @ResponseBody
    UploadResDto removeStu(Integer claId, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        try {
            ExcelResultDto dto = null;
            if(files.length<=1) {
                dto = batchOperateStuTools.batchRemoveStu(files[0].getInputStream(), claId);
            } else {
                dto = new ExcelResultDto(0, 0, 0, "");
            }
            return new UploadResDto(UploadResDto.SUC, JSON.toJSONString(dto));
        } catch (Exception e) {
//            throw new SystemException("文件解析出错");
            return new UploadResDto(UploadResDto.ERROR, e.getMessage());
        }
    }
}

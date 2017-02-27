package com.zslin.basic.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.model.Menu;
import com.zslin.basic.model.Role;
import com.zslin.basic.model.RoleMenu;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.service.IMenuService;
import com.zslin.basic.service.IRoleMenuService;
import com.zslin.basic.service.IRoleService;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色管理Controller
 * @author zslin.com 20160513
 *
 */
@Controller
@RequestMapping(value="admin/role")
@AdminAuth(name = "角色管理", psn="权限管理", orderNum = 2, pentity=0, porderNum=2)
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleMenuService roleMenuService;

    /** 列表 */
    @AdminAuth(name = "角色管理", orderNum = 1, icon="fa fa-users", type="1")
    @RequestMapping(value="list", method= RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Role> datas = roleService.findAll(new ParamFilterUtil<Role>().buildSearch(model, request), SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/basic/role/list";
    }

    @AdminAuth(name="角色授权", orderNum=5)
    @RequestMapping(value="menus/{id}", method=RequestMethod.GET)
    public String menus(Model model, @PathVariable Integer id, HttpServletRequest request) {
        String treeJson = queryTreeJson(null);
        model.addAttribute("role", roleService.findOne(id)); //获取当前角色

        List<Integer> curAuthList = roleService.listRoleMenuIds(id);
        StringBuffer sb = new StringBuffer();
        for(Integer aid : curAuthList) {sb.append(aid).append(",");}
        sb.append("0");
        model.addAttribute("curAuth", sb.toString()); //获取角色已有的菜单id
        model.addAttribute("treeJson", treeJson);
        return "admin/basic/role/menus";
    }

    /** 添加角色 */
    @Token(flag= Token.READY)
    @AdminAuth(name = "添加角色", orderNum = 2, icon="icon-plus")
    @RequestMapping(value="add", method=RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        Role role = new Role();
        model.addAttribute("role", role);
        return "admin/basic/role/add";
    }

    /** 添加角色POST */
    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Role role, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            role.setSn(PinyinToolkit.cn2Spell(role.getName(), ""));
            roleService.save(role);
        }
        return "redirect:/admin/role/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改角色", orderNum=3)
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Role r = roleService.findOne(id);
        model.addAttribute("role", r);
        return "admin/basic/role/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Role role, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            Role r = roleService.findOne(id);
            r.setName(role.getName());
            roleService.save(r);
        }
        return "redirect:/admin/role/list";
    }

    @AdminAuth(name="删除角色", orderNum=4)
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable Integer id) {
        try {
            roleService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping(value = "addOrDelRoleMenu", method = RequestMethod.POST)
    @AdminAuth(name="为角色授权资源", orderNum=5)
    public @ResponseBody String addOrDelRoleMenu(Integer roleId, Integer menuId) {
        try {
            RoleMenu rm = roleMenuService.queryByRidAndMid(roleId, menuId);
            if(rm==null) {
                rm = new RoleMenu();
                rm.setMid(menuId); rm.setRid(roleId);
                roleMenuService.save(rm);
            } else {
                roleMenuService.delete(rm);
            }

        } catch (Exception e) {
            throw new SystemException("为角色授权资源失败");
        }
        return "1";
    }

    /**
     * 获取菜单数据，以JSON数据返回
     * @param type 菜单类型，1：导航菜单；2：权限菜单；其他：全部
     * @return
     */
    public String queryTreeJson(String type) {
        return buildTreeJson(null, type);
    }

    /**
     * 生成JSON数据
     * @param pid 父Id
     * @param type 类型
     * @return
     */
    private String buildTreeJson(Integer pid, String type) {
        List<Menu> plist = this.listByPid(pid, type);
        if(plist!=null && plist.size()>0) {
            Integer index = 0;
            StringBuffer sb = new StringBuffer();
            if(pid!=null && pid>0) {sb.append(",nodes:");}
            sb.append("[");
            for(Menu pm : plist) {
                index++;
                sb.append("{text:'<span title=").append(pm.getId()).append(">").append(pm.getName()).append("</span>', ")
                        .append("href:'javascript:targetHref(").append(pm.getId()).append(")'");

                //递归获取数据
                sb.append(buildTreeJson(pm.getId(), type));

                sb.append("}");
                if(index<plist.size()) {sb.append(",");}
            }
            sb.append("]");
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 通过父Id，及菜单类型获取菜单数据
     * @param pid 父Id,如果为空则获取最高层的菜单
     * @param type 菜单类型，1：导航菜单；2：权限菜单
     * @return
     */
    public List<Menu> listByPid(Integer pid, String type) {
        String hql = "FROM Menu m WHERE 1=1 ";
        if(pid!=null && pid>0) {hql += " AND m.pid="+pid;}
        else {hql += " AND m.pid is null ";}
        if(type!=null && ("1".equals(type) || "2".equals(type))) {
            hql += " AND m.type='"+type+"'";
        }
        hql += " ORDER BY m.orderNum ASC";
//        return em.createQuery(hql).getResultList();
        return menuService.listByHql(hql);
    }
}
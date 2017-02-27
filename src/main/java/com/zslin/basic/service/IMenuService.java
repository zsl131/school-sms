package com.zslin.basic.service;

import com.zslin.basic.model.Menu;
import com.zslin.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl-pc on 2016/9/1.
 */
public interface IMenuService extends BaseRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.display=1 AND m.type='1' AND m.id in (SELECT rm.mid FROM RoleMenu rm WHERE rm.rid IN (SELECT ur.rid FROM UserRole ur where ur.uid=?1))")
    List<Menu> findByUser(Integer userId);

    Menu findBySn(String sn);

    @Query("SELECT m.sn FROM Menu m WHERE m.display=1 AND m.id in (SELECT rm.mid FROM RoleMenu rm WHERE rm.rid IN (SELECT ur.rid FROM UserRole ur where ur.uid=?1))")
    List<String> listAuthByUser(Integer userId);

    @Query("FROM Menu m WHERE m.href is not null AND m.href!='' AND m.href!='#' ")
    List<Menu> listAllUrlMenu();
}

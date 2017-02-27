package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Teacher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 14:22.
 */
@Service("teacherService")
public interface ITeacherService extends BaseRepository<Teacher, Integer>, JpaSpecificationExecutor<Teacher> {

    List<Teacher> findBySchId(Integer schId);
}

package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.MyTask;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/19 16:37.
 */
@Service("myTaskService")
public interface IMyTaskService extends BaseRepository<MyTask, Integer>, JpaSpecificationExecutor<MyTask> {

    @Query("UPDATE MyTask mt SET mt.status=?2 WHERE mt.id=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer id, String status);

    @Query("FROM MyTask mt WHERE mt.status='0'")
    List<MyTask> findNoRun();
}

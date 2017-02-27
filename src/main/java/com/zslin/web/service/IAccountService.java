package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:27.
 */
public interface IAccountService extends BaseRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

    @Query("UPDATE Account a SET a.status=?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer id, String status);

    @Query("UPDATE Account a SET a.status=?2 WHERE a.openid=?1")
    @Modifying
    @Transactional
    void updateStatus(String openid, String status);

    Account findByOpenid(String openid);
}

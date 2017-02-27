package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Classes;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 14:22.
 */
public interface IClassesService extends BaseRepository<Classes, Integer>, JpaSpecificationExecutor<Classes> {
}

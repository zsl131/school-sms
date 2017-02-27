package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.School;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 14:21.
 */
public interface ISchoolService extends BaseRepository<School, Integer>, JpaSpecificationExecutor<School> {
}

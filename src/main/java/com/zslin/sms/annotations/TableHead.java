package com.zslin.sms.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 17:21.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TableHead {

    /** 用于显示的中文名称 */
    String name();

    /** 对应的ServiceName */
    String serviceName() default "";

    /** 用于与数据表对应的字段 */
    String field() default "";
}

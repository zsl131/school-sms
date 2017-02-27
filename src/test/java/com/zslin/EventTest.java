package com.zslin;

import com.zslin.wx.tools.EventTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 10:30.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class EventTest {

    @Autowired
    private EventTools eventTools;

    @Test
    public void testSendEvent() {
        boolean b = eventTools.eventRemind("oxsm0wlWzgKXwW-EERaTgV_dvufs", "标题", "事件类型", "2017-02-08", "事件内容\\n换行试试", "http://www.baidu.com");
        System.out.println("======="+b);
    }
}

package com.zslin;

import com.zslin.sms.tools.SmsTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 11:06.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class SmsTest {

    @Autowired
    private SmsTools smsTools;

    @Test
    public void testAddModule() {
        smsTools.addModule("钟述林", "你好，你的验证码是#code#。");
    }

    @Test
    public void testDelModule() {
        smsTools.deleteModule(25);
    }

    @Test
    public void testListModule() {
        smsTools.listModules();
    }

    @Test
    public void testSurplus() {
        smsTools.surplus();
    }

    @Test
    public void testSendMsg() {
        smsTools.sendMsg(1, "", "name", "张三");
        smsTools.sendMsg(1, "", "name", "张三", "code", "123456");
        smsTools.sendMsg(1, "", "name", "张三", "code", "123456", "age", "28");
    }
}

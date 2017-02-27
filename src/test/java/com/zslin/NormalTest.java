package com.zslin;

import com.zslin.basic.tools.ConfigTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URLEncoder;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/12 9:09.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class NormalTest {

    @Autowired
    private ConfigTools configTools;

    @Test
    public void test() {
        System.out.println("========="+configTools.getName());
    }

    @Test
    public void testEncode() throws Exception {
        System.out.println(URLEncoder.encode("{'mid':'7', 'phone':'','con':'#name#=李才配&#code#=123456'}", "utf-8"));
    }
}

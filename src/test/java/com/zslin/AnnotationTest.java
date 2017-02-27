package com.zslin;

import com.zslin.sms.dto.DataDto;
import com.zslin.sms.tools.AnnotationTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 17:31.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class AnnotationTest {

    @Autowired
    private AnnotationTools annotationTools;

    @Test
    public void test() {
        List<DataDto> list = annotationTools.buildTableHeads();
        for(DataDto dto : list) {
            System.out.println(dto);
        }
    }

    @Test
    public void testField() {
        List<DataDto> list = annotationTools.buildAttr("com.zslin.web.model.Student");
        for(DataDto dto : list) {
            System.out.println(dto);
        }
    }

    @Test
    public void testServiceName() {
        String cn = annotationTools.getServiceName("com.zslin.web.model.Student");
        System.out.println("==="+cn);
    }
}

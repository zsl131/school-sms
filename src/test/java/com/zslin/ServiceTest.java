package com.zslin;

import com.zslin.sms.tools.DatabaseTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/18 22:50.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class ServiceTest {

    @Autowired
    BeanFactory bf;

    @Autowired
    private DatabaseTools databaseTools;

    @Test
    public void test() {
        System.out.println("========="+bf);
        Object serviceObj = bf.getBean("teacherService");
        System.out.println(serviceObj);

    }

    @Test
    public void testDatas() throws Exception {
        //mid:3,sourceType:2,token:com.zslin.web.model.Teacher,pars:phone=com.zslin.web.model.Teacher.phone&#code#=com.zslin.web.model.Teacher.identity&
        Class.forName("com.zslin.web.model.Student");
        Object serviceObj = bf.getBean("studentService");
        Method method = serviceObj.getClass().getMethod("findAll");
        List<Object> result = (List<Object>) method.invoke(serviceObj);
        System.out.println(result.size());
    }

    @Test
    public void testQuery() {
        String pn = "com.zslin.web.model.Student";
        List list = databaseTools.buildListByPackageName(pn, "com.zslin.web.model.Teacher.phone");
        for(Object obj : list) {
            System.out.println("phone==="+obj);
        }

        List list2 = databaseTools.buildListByPackageName(pn, "com.zslin.web.model.Teacher.identity");
        for(Object obj : list2) {
            System.out.println("identity==="+obj);
        }
    }
}

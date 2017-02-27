package com.zslin.sms.tools;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/19 9:40.
 * 以数据库作为短信发送的数据源的处理工具
 */
@Component
public class DatabaseTools {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private AnnotationTools annotationTools;

    private Map<String, List> datas;

    public DatabaseTools() {
        if(datas == null) {
            datas = new HashMap<>();
        }
    }

    public List queryDatasByPackageName(String packageName) {
        String serviceName = annotationTools.getServiceName(packageName);
        return queryDatas(serviceName);
    }

    public List queryDatas(String serviceName) {
        List result = datas.get(serviceName);
        if(result==null) {
            try {
                Object serviceObj = beanFactory.getBean(serviceName);
                Method method = serviceObj.getClass().getMethod("findAll");
                result = (List) method.invoke(serviceObj);
                datas.put(serviceName, result);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List buildListByPackageName(String packageName, String field) {
        String serviceName = annotationTools.getServiceName(packageName);
        return buildList(serviceName, field);
    }

    public List buildList(String serviceName, String field) {
        List result = new ArrayList();
        try {
            field = rebuildFieldName(field);
            List objList = queryDatas(serviceName);
            for(Object obj : objList) {
                result.add(buildValue(obj, field));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String rebuildFieldName(String field) {
        if(field.indexOf(".")>=0) {
            return field.substring(field.lastIndexOf(".")+1, field.length());
        }
        return field;
    }

    public Object buildValueByPackageName(Object obj, String packageName) throws Exception {
        String field = rebuildFieldName(packageName);
        return buildValue(obj, field);
    }

    public Object buildValue(Object obj, String field) throws Exception {
        Method method = obj.getClass().getMethod(getMethodName(field));
        return method.invoke(obj);
    }

    private String getMethodName(String field) {
        return "get"+field.substring(0, 1).toUpperCase()+field.substring(1, field.length());
    }
}

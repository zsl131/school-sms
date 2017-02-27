package com.zslin.sms.tools;

import com.zslin.sms.annotations.TableHead;
import com.zslin.sms.dto.DataDto;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 17:26.
 * 获取数据库数据库的工具类
 */
@Component
public class AnnotationTools {

    /**
     * 获取哪些类可以作为数据库
     * @return
     */
    public List<DataDto> buildTableHeads() {
        List<DataDto> result = new ArrayList<>();
        try {
            //com.zslin.web.model
//            String pn = "classpath*:com/zslin/*/model/*.class";
            String pn = "classpath*:com/zslin/**/*.class";
            //1、创建ResourcePatternResolver资源对象
            ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();
            //2、获取路径中的所有资源对象
            Resource[] ress = rpr.getResources(pn);
            //3、创建MetadataReaderFactory来获取工程
            MetadataReaderFactory fac = new CachingMetadataReaderFactory();

            //4、遍历资源
            for(Resource res:ress) {
                MetadataReader mr = fac.getMetadataReader(res);
                String cname = mr.getClassMetadata().getClassName();
                AnnotationMetadata am = mr.getAnnotationMetadata();
                if(am.hasAnnotation(TableHead.class.getName())) {
//                    System.out.println(am.getClassName()+"===="+cname);
                    Map<String, Object> classRes = am.getAnnotationAttributes(TableHead.class.getName());
//                    System.out.println(classRes.get("name")+"========="+classRes.get("field"));
                    String field = (String) classRes.get("field");
                    result.add(new DataDto(cname, field==null||"".equals(field)?getName(cname):field, (String) classRes.get("name")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取对象对应的ServiceName
     * @param packageName
     * @return
     */
    public String getServiceName(String packageName) {
        try {
            TableHead th = Class.forName(packageName).getAnnotation(TableHead.class);
            String serviceName = th.serviceName();
            if(serviceName==null || "".equals(serviceName)) {
                String cName = getName(packageName);
                return cName.substring(0, 1).toLowerCase()+cName.substring(1, cName.length())+"Service";
            } else {
                return serviceName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取属性上的值
     * @param packageName
     * @return
     */
    public List<DataDto> buildAttr(String packageName) {
        List<DataDto> result = new ArrayList<>();
        try {
            Field [] fields = Class.forName(packageName).getDeclaredFields();
            for(Field f : fields) {
                if(f.isAnnotationPresent(TableHead.class)) {
                    TableHead th = f.getAnnotation(TableHead.class);
                    result.add(new DataDto(packageName, th.field()==null||"".equals(th.field())?f.getName():th.field(), th.name()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getName(String packageName) {
        if(packageName.indexOf(".")>=0) {
            return packageName.substring(packageName.lastIndexOf(".")+1, packageName.length());
        }
        return packageName;
    }
}

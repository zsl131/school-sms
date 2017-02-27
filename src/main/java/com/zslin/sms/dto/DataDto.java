package com.zslin.sms.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/15 9:56.
 * 用于群发短信数据源为数据库数据的DTO对象
 */
public class DataDto {

    /**
     * 包名，如：com.zslin.web.model.Teacher
     */
    private String packageName;

    /**
     * 类名，如：Teacher
     */
    private String className;

    /**
     * 显示名称，如：老师
     */
    private String showName;

    @Override
    public String toString() {
        return "DataDto{" +
                "packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", showName='" + showName + '\'' +
                '}';
    }

    public DataDto() {}

    public DataDto(String packageName, String className, String showName) {
        this.packageName = packageName;
        this.className = className;
        this.showName = showName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }
}

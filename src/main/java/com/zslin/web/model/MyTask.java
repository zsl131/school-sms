package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/19 16:09.
 */
@Entity
@Table(name = "t_my_task")
public class MyTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 名称 */
    private String name;

    /** 描述 */
    private String remark;

    /** 执行类的包名，包含类名 */
    @Column(name = "package_name")
    private String packageName;

    /** 执行方法的名称 */
    @Column(name = "method_name")
    private String methodName;

    /** 调用方法的参数 */
    private String params;

    /**
     * 处理方式：0-单次执行，需要指定执行时间；1-按间隔时间执行；2-按指定时间执行
     */
    @Column(name = "process_type")
    private String processType;

    /** 第一次执行的时间，格式：yyyy-MM-dd HH:mm:ss */
    @Column(name = "first_time")
    private String firstTime;

    /** 执行次数 */
    private Integer times;

    /** 时间间隔，单位：秒 */
    @Column(name = "time_count")
    private Long timeCount;

    /** 状态，-1-已终止；0-未开始；1-进行中 */
    private String status="0";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Long getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(Long timeCount) {
        this.timeCount = timeCount;
    }
}

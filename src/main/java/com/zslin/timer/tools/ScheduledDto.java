package com.zslin.timer.tools;

import java.util.concurrent.ScheduledFuture;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/22 10:33.
 */
public class ScheduledDto {
    private String id;
    private ScheduledFuture future;

    private String remark;

    public ScheduledDto(String id, ScheduledFuture future) {
        this.id = id;
        this.future = future;
        this.remark = "系统默认";
    }

    public ScheduledDto(String id, ScheduledFuture future, String remark) {
        this.id = id;
        this.future = future;
        this.remark = (remark==null||"".equals(remark))?"系统默认":remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getId().equals(((ScheduledDto) obj).getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ScheduledFuture getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture future) {
        this.future = future;
    }
}

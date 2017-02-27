package com.zslin.web.tools;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/13 16:11.
 * 上传Excel结果DTO对象
 */
public class ExcelResultDto {

    private int total=0;
    private int suc=0;
    private int err=0;
    private String msg = "";
    public ExcelResultDto(int total, int suc, int err, String msg) {
        this.total = total;
        this.suc = suc;
        this.err = err;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ExcelResultDto{" +
                "total=" + total +
                ", suc=" + suc +
                ", err=" + err +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuc() {
        return suc;
    }

    public void setSuc(int suc) {
        this.suc = suc;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

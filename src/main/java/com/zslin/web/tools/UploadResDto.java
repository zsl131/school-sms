package com.zslin.web.tools;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/13 11:47.
 * 上传Excel等文件后的结果返回DTO对象
 */
public class UploadResDto {

    public static final String SUC = "0";

    public static final String ERROR = "1";

    private String status;

    private String msg;

    public UploadResDto(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

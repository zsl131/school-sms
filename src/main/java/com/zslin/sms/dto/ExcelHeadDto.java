package com.zslin.sms.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/15 16:18.
 * 批量发短信上传Excel表头的DTO对象
 */
public class ExcelHeadDto {

    /** 列号 */
    private Integer no;

    /** 值 */
    private String field;

    public ExcelHeadDto() {}

    public ExcelHeadDto(Integer no, String field) {
        this.no = no;
        this.field = field;
    }

    @Override
    public String toString() {
        return "ExcelHeadDto{" +
                "no=" + no +
                ", field='" + field + '\'' +
                '}';
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

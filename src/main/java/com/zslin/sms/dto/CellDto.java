package com.zslin.sms.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/15 15:23.
 * Excel的单元格DTO对象
 */
public class CellDto {

    private Integer no;

    private String field;

    private String value;

    public CellDto() {}

    public CellDto(Integer no, String field, String value) {
        this.no = no;
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return "CellDto{" +
                "no=" + no +
                ", field='" + field + '\'' +
                ", value='" + value + '\'' +
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

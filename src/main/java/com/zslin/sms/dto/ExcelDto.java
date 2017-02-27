package com.zslin.sms.dto;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/15 16:18.
 * 批量发短信上传的Excel解析DTO对象
 */
public class ExcelDto {

    private String token;

    private List<ExcelHeadDto> headDtoList;

    private List<RowDto> rowDtoList;

    public ExcelDto() {}

    public ExcelDto(String token, List<ExcelHeadDto> headDtoList, List<RowDto> rowDtoList) {
        this.headDtoList = headDtoList;
        this.rowDtoList = rowDtoList;
    }

    public List<ExcelHeadDto> getHeadDtoList() {
        return headDtoList;
    }

    public void setHeadDtoList(List<ExcelHeadDto> headDtoList) {
        this.headDtoList = headDtoList;
    }

    public List<RowDto> getRowDtoList() {
        return rowDtoList;
    }

    public void setRowDtoList(List<RowDto> rowDtoList) {
        this.rowDtoList = rowDtoList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

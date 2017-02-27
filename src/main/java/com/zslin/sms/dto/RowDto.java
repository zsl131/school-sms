package com.zslin.sms.dto;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/15 15:23.
 * Excel 行的DTO对象
 */
public class RowDto {

    private Integer no;

    List<CellDto> cellList;

    public RowDto() {}

    public RowDto(Integer no, List<CellDto> cellList) {
        this.no = no;
        this.cellList = cellList;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public List<CellDto> getCellList() {
        return cellList;
    }

    public void setCellList(List<CellDto> cellList) {
        this.cellList = cellList;
    }
}

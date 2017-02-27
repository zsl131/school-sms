package com.zslin.web.tools;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.text.DecimalFormat;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/15 16:26.
 */
public class ExcelNormalTools {

    public static String buildContent(HSSFCell cell) {
        String con = "";
        try {
            if(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                con = "";
            } else if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                con = cell.getStringCellValue();
            } else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                con = cell.getNumericCellValue()+"";
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return con;
    }

    public static String buildContent(HSSFCell cell, boolean maybeNumber) {
        String result = buildContent(cell);
        if(maybeNumber) {
            try {
                Double d = Double.parseDouble(result);
                result = rebuildStr(d);
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static String rebuildStr(Double str) {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(str);
    }

}

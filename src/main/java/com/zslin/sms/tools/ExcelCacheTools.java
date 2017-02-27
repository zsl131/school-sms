package com.zslin.sms.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.cache.CacheTools;
import com.zslin.sms.dto.CellDto;
import com.zslin.sms.dto.ExcelDto;
import com.zslin.sms.dto.ExcelHeadDto;
import com.zslin.sms.dto.RowDto;
import com.zslin.web.tools.ExcelNormalTools;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/15 15:27.
 * 上传Excel将数据解析到Cache中的工具类
 */
@Component
public class ExcelCacheTools {

    @Autowired
    private CacheTools cacheTools;

    @Autowired
    private ConfigTools configTools;

    private String copyFiles(InputStream is, String fileName) {
        try {
            String fileType = getFileType(fileName); //文件类型
            String fn = UUID.randomUUID().toString()+fileType;
            String myFile = configTools.getUploadPath("/temp")+fn;
            FileOutputStream fos1 = new FileOutputStream(new File(myFile));
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len=is.read(bytes))!=-1) {
                fos1.write(bytes, 0, len);
            }
            fos1.flush();
            fos1.close();

            return myFile;
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    private String getFileType(String fileName) {
        if(fileName.indexOf(".")>=0) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return fileName;
    }

    public ExcelDto uploadExcel(InputStream is, String fileName, boolean cache) {
        ExcelDto result = null;
        try {
            String filePath = copyFiles(is, fileName);
            File file = new File(filePath);
            String token = FileMD5Tools.fileMD5(file); //获取md5值

            result = uploadExcel(new FileInputStream(file), token);
            result.setToken(token);
            file.delete();
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
        return result;
    }

    private ExcelDto uploadExcel(InputStream is, String token) {
        ExcelDto dto = uploadExcel(is);
        if(token!=null && !"".equals(token)) {
            String jsonStr = JSON.toJSONString(dto);
            cacheTools.putKey(token, jsonStr, 60 * 60);
        }
        return dto;
    }

    public ExcelDto queryFromCache(String md5) {
        String json = (String) cacheTools.getKey(md5);
        ExcelDto dto = JSON.toJavaObject(JSON.parseObject(json), ExcelDto.class);
        return dto;
    }

    public void resaveCache(String md5, Integer timeout) {
        String json = (String) cacheTools.getKey(md5);
        cacheTools.putKey(md5, json, timeout);
    }

    public List<Map<String, String>> buildDataRows(String md5) {
        List<Map<String, String>> result = new ArrayList<>();
        ExcelDto dto = queryFromCache(md5);
        for(RowDto row : dto.getRowDtoList()) {
            Map<String, String> map = new HashMap<>();
            for(CellDto cd : row.getCellList()) {
                map.put(cd.getNo()+"", cd.getValue());
            }
            result.add(map);
        }
        return result;
    }

    private ExcelDto uploadExcel(InputStream is) {
        ExcelDto result = null;
        List<ExcelHeadDto> headList = null;
        List<RowDto> rowList = new ArrayList<>();
        String token = "";
        try {
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            headList = buildHeads(sheet.getRow(1)); //第二行为表头
            for(int i=2; i<sheet.getLastRowNum()+1; i++) {
                HSSFRow row = sheet.getRow(i);
                List<CellDto> cellList = buildCells(row, headList);
                if(cellList!=null) {
                    rowList.add(new RowDto(row.getRowNum(), cellList));
                }
            }
            result = new ExcelDto(token, headList, rowList);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new SystemException(e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return result;
    }

    private List<CellDto> buildCells(HSSFRow row, List<ExcelHeadDto> headList) {
        List<CellDto> result = new ArrayList<>();
        for(int i=0; i<row.getLastCellNum(); i++) {
            HSSFCell cell = row.getCell(i);
            String field = ExcelNormalTools.buildContent(cell, true);
            try { //取head的时候有可能有异常
                result.add(new CellDto(cell.getColumnIndex(), headList.get(i).getField(), field));
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        if(isNullRow(result)) {return null;}
        return result;
    }

    private boolean isNullRow(List<CellDto> cellList) {
        boolean result = true;
        for(CellDto dto : cellList) {
            if(dto.getValue()!=null && !"".equals(dto.getValue())) {
                result = false; break;
            }
        }
        return result;
    }

    private List<ExcelHeadDto> buildHeads(HSSFRow headRow) {
        List<ExcelHeadDto> result = new ArrayList<>();
        for(int i=0; i<headRow.getLastCellNum(); i++) {
            HSSFCell cell = headRow.getCell(i);
            String field = ExcelNormalTools.buildContent(cell, true);
            if(field==null || "".equals(field.trim())) {continue;}
            result.add(new ExcelHeadDto(cell.getColumnIndex(), field));
        }
        return result;
    }
}

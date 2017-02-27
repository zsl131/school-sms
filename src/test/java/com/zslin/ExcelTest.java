package com.zslin;

import com.zslin.sms.dto.CellDto;
import com.zslin.sms.dto.ExcelDto;
import com.zslin.sms.dto.ExcelHeadDto;
import com.zslin.sms.dto.RowDto;
import com.zslin.sms.tools.ExcelCacheTools;
import com.zslin.web.tools.BatchOperateStuTools;
import com.zslin.web.tools.ExcelResultDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/13 14:53.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class ExcelTest {

    @Autowired
    private BatchOperateStuTools batchAddStuTools;

    @Autowired
    private ExcelCacheTools excelCacheTools;

    @Test
    public void test() throws Exception {
        ExcelResultDto dto = batchAddStuTools.batchAddStu(new FileInputStream(new File("D:/temp/add-stu-template.xls")), 3);
        System.out.println(dto);
    }

    @Test
    public void testFile() throws Exception {
        ExcelDto dto = excelCacheTools.uploadExcel(new FileInputStream(new File("D:/temp/add-stu-template.xls")), "add-stu-template.xls", true);
        System.out.println(dto.getToken());
        for(ExcelHeadDto d : dto.getHeadDtoList()) {
            System.out.println("head:"+d);
        }
        for(RowDto rd : dto.getRowDtoList()) {
            System.out.println(rd.getNo()+"=====");
            for(CellDto cd : rd.getCellList()) {
                System.out.println("cell:"+cd);
            }
        }
    }

    @Test
    public void testDto() {
        ExcelDto dto = excelCacheTools.queryFromCache("da52c5494d17fd0fcec3bec16de981c1");
        List<ExcelHeadDto> headList = dto.getHeadDtoList();
        List<RowDto> rowList = dto.getRowDtoList();
        for(ExcelHeadDto hd : headList) {
            System.out.println(hd);
        }
        for(RowDto rd : rowList) {
            System.out.print("============="+rd.getNo());
            for (CellDto cd : rd.getCellList()) {
                System.out.println(cd);
            }
        }
    }
}

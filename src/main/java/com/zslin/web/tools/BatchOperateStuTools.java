package com.zslin.web.tools;

import com.zslin.basic.exception.SystemException;
import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.Classes;
import com.zslin.web.model.Student;
import com.zslin.web.service.IClassesService;
import com.zslin.web.service.IStudentService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/13 11:57.
 * 批量操作学生工具类
 */
@Component
public class BatchOperateStuTools {

    @Autowired
    private IStudentService studentService;

    @Autowired
    private IClassesService classesService;

    /**
     * 批量删除学生信息
     * @param is
     * @param claId
     * @return
     */
    public ExcelResultDto batchRemoveStu(InputStream is, Integer claId) {
        try {
            StringBuffer sb = new StringBuffer();
            int total = 0, suc = 0, err = 0;
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            for(int i=2; i<sheet.getLastRowNum()+1; i++) {
                HSSFRow row = sheet.getRow(i);
                String res = removeStu(row, claId);
                total ++;
                if("".equals(res)) {suc ++;}
                else {
                    err ++;
                    sb.append(res).append("\n");
                }
            }
            return new ExcelResultDto(total, suc, err, sb.toString());
        } catch (IOException e) {
//            e.printStackTrace();
            throw new SystemException(e.getMessage());
        }
    }

    private String removeStu(HSSFRow row, Integer claId) {
        String no = "";
        HSSFCell c1 = row.getCell(0); //no
        no = ExcelNormalTools.buildContent(c1, true);
        /*if(c1.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
            no = "";
        } else if(c1.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            no = c1.getStringCellValue();
        } else if(c1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            no = rebuildStr(c1.getNumericCellValue());
        }*/
        if(no==null || "".equals(no.trim())) {
            return "第["+(row.getRowNum()+1)+"]行学号为空";
        } else {
            Integer id = studentService.findId(no, claId);
            if(id==null || id<=0) {
                return "第["+(row.getRowNum()+1)+"]行学号“"+no+"”不存在";
            } else {
                studentService.delete(id); //删除学生信息
                return "";
            }
        }
    }

    /**
     * 批量添加学生信息
     * @param is
     * @param claId
     * @return
     */
    public ExcelResultDto batchAddStu(InputStream is, Integer claId) {
        try {
            StringBuffer sb = new StringBuffer();
            int total = 0, suc = 0, err = 0;
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            for(int i=2; i<sheet.getLastRowNum()+1; i++) {
                HSSFRow row = sheet.getRow(i);
                String res = addStu(row, claId);
                total ++;
                if("".equals(res)) {suc ++;}
                else {
                    err ++;
                    sb.append(res).append("\n");
                }
            }
            return new ExcelResultDto(total, suc, err, sb.toString());
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
    }

    private String addStu(HSSFRow row, Integer claId) {
        try {
            String no =""; //1
            String name = ""; //2
            String sex=""; //3
            String identity=""; //4
            String phone=""; //5
            String fatherName=""; //6
            String fatherPhone=""; //7
            String motherName=""; //8
            String motherPhone=""; //9
            String address=""; //10
            HSSFCell c1 = row.getCell(0); //no
            no = ExcelNormalTools.buildContent(c1, true);
            /*if(c1.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                no = "";
            } else if(c1.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                no = c1.getStringCellValue();
            } else if(c1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                no = rebuildStr(c1.getNumericCellValue());
            }*/

            HSSFCell c2 = row.getCell(1); //
            name = ExcelNormalTools.buildContent(c2);
            /*if(c2.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                name = "";
            } else if(c2.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                name = c2.getStringCellValue();
            } else if(c2.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                name = c2.getNumericCellValue()+"";
            }*/

            HSSFCell c3 = row.getCell(2); //
            sex = ExcelNormalTools.buildContent(c3);
            /*if(c3.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                sex = "";
            } else if(c3.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                sex = c3.getStringCellValue();
            } else if(c3.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                sex = c3.getNumericCellValue()+"";
            }*/
//            System.out.println("1======sex:"+sex);
            sex = "男".equals(sex)?"1":"2";
//            System.out.println("2======sex:"+sex);

            HSSFCell c4 = row.getCell(3); //
            identity = ExcelNormalTools.buildContent(c4, true);
            /*if(c4.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                identity = "";
            } else if(c4.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                identity = c4.getStringCellValue();
            } else if(c4.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                identity = c4.getNumericCellValue()+"";
            }*/

            HSSFCell c5 = row.getCell(4); //
            phone = ExcelNormalTools.buildContent(c5, true);
            /*if(c5.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                phone = "";
            } else if(c5.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                phone = c5.getStringCellValue();
            } else if(c5.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                phone = rebuildStr(c5.getNumericCellValue());
            }*/

            HSSFCell c6 = row.getCell(5); //
            fatherName = ExcelNormalTools.buildContent(c6);
            /*if(c6.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                fatherName = "";
            } else if(c6.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                fatherName = c6.getStringCellValue();
            } else if(c6.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                fatherName = c6.getNumericCellValue()+"";
            }*/

            HSSFCell c7 = row.getCell(6); //
            fatherPhone = ExcelNormalTools.buildContent(c7, true);
            /*if(c7.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                fatherPhone = "";
            } else if(c7.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                fatherPhone = c7.getStringCellValue();
            } else if(c7.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                fatherPhone = rebuildStr(c7.getNumericCellValue());
            }*/

            HSSFCell c8 = row.getCell(7); //
            motherName = ExcelNormalTools.buildContent(c8);
            /*if(c8.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                motherName = "";
            } else if(c8.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                motherName = c8.getStringCellValue();
            } else if(c8.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                motherName = c8.getNumericCellValue()+"";
            }*/

            HSSFCell c9 = row.getCell(8); //
            motherPhone = ExcelNormalTools.buildContent(c9, true);
            /*if(c9.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                motherPhone = "";
            } else if(c9.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                motherPhone = c9.getStringCellValue();
            } else if(c9.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                motherPhone = rebuildStr(c9.getNumericCellValue());
            }*/

            HSSFCell c10 = row.getCell(9); //
            address = ExcelNormalTools.buildContent(c10);
            /*if(c10.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                address = "";
            } else if(c10.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                address = c10.getStringCellValue();
            } else if(c10.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                address = c10.getNumericCellValue()+"";
            }*/

            Student student = new Student();
            student.setSex(sex);
            student.setPhone(phone);
            student.setIdentity(identity);
            student.setAddress(address);
            student.setFatherName(fatherName);
            student.setFatherPhone(fatherPhone);
            student.setMotherName(motherName);
            student.setMotherPhone(motherPhone);
            student.setName(name);
            student.setStuNo(no);

            student.setCreateDate(new Date());
            student.setCreateLong(System.currentTimeMillis());
            student.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            student.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));

            if(no==null || "".equals(no.trim()) || name==null || "".equals(name.trim())) {
                return "第["+(row.getRowNum()+1)+"]行学号或姓名为空";
            }

            Student s = studentService.findByStuNoAndClaId(no, claId);
            if(s!=null) {
                return "第["+(row.getRowNum()+1)+"]行学号“"+no+"”已经存在";
            }

            Classes cla = classesService.findOne(claId);
            if(cla!=null) {
                student.setClaId(cla.getId());
                student.setClaName(cla.getName());
                student.setSchId(cla.getSchId());
                student.setSchName(cla.getSchName());
            }
            studentService.save(student);
            return "";
        } catch (Exception e) {
            throw new SystemException("文件解析出错，请检查上传的文件是否正确");
        }
    }
}

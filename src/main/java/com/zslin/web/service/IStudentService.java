package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 14:22.
 */
@Service("studentService")
public interface IStudentService extends BaseRepository<Student, Integer>, JpaSpecificationExecutor<Student> {

    //通过学号和班级ID获取学生对象
    Student findByStuNoAndClaId(String stuNo, Integer claId);

    @Query("SELECT s.id FROM Student s WHERE s.stuNo=?1 AND s.claId=?2")
    Integer findId(String stuNo, Integer claId);
}

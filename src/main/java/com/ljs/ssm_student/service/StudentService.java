package com.ljs.ssm_student.service;

import com.ljs.ssm_student.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentService {

    Student findStudentByStudentName(String username);

    int saveStudent(Student Student);

    List<Student> findList(Map<String, Object> queryConditions);

    int getTotal(String username);

    int editStudent(Student student);

    int deleteByIds(@Param("ids") Integer[] ids);

}

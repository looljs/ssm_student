package com.ljs.ssm_student.service.impl;

import com.ljs.ssm_student.dao.StudentDAO;
import com.ljs.ssm_student.entity.Student;
import com.ljs.ssm_student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDAO studentDAO;

    @Override
    public Student findStudentByStudentName(String username) {
        return studentDAO.findStudentByStudentName(username);
    }

    @Override
    public int saveStudent(Student student) {
        return studentDAO.saveStudent(student);
    }

    @Override
    public List<Student> findList(Map<String, Object> queryConditions) {
        return studentDAO.findList(queryConditions);
    }

    @Override
    public int getTotal(String username) {
        return studentDAO.getTotal(username);
    }

    @Override
    public int editStudent(Student student) {
        return studentDAO.editStudent(student);
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        return studentDAO.deleteByIds(ids);
    }
}

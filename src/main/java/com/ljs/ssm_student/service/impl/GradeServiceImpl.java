package com.ljs.ssm_student.service.impl;

import com.ljs.ssm_student.dao.GradeDAO;
import com.ljs.ssm_student.entity.Grade;
import com.ljs.ssm_student.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeDAO gradeDAO;

    @Override
    public Grade findGradeByName(String name) {
        return gradeDAO.findGradeByGradeName(name);
    }

    @Override
    public int saveGrade(Grade grade) {
        return gradeDAO.saveGrade(grade);
    }

    @Override
    public List<Grade> findList(Map<String, Object> queryConditions) {
        return gradeDAO.findList(queryConditions);
    }

    @Override
    public int getTotal(String name) {
        return gradeDAO.getTotal(name);
    }

    @Override
    public int editUser(Grade grade) {
        return gradeDAO.editGrade(grade);
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        return gradeDAO.deleteByIds(ids);
    }

    @Override
    public boolean gradeIsAir(Integer[] ids) {
        int i = gradeDAO.gradeIsAir(ids);
        return i <= 0;
    }

    @Override
    public List<Grade> findAll() {
        return gradeDAO.findAll();
    }
}

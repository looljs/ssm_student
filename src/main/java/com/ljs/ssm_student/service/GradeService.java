package com.ljs.ssm_student.service;

import com.ljs.ssm_student.entity.Grade;

import java.util.List;
import java.util.Map;

public interface GradeService {

    Grade findGradeByName(String name);

    int saveGrade(Grade grade);

    List<Grade> findList(Map<String, Object> queryConditions);

    int getTotal(String name);

    int editUser(Grade grade);

    int deleteByIds(Integer[] ids);

    boolean gradeIsAir(Integer[] ids);

    List<Grade> findAll();
}

package com.ljs.ssm_student.service;


import com.ljs.ssm_student.entity.Clazz;

import java.util.List;
import java.util.Map;

public interface ClazzService {

    Clazz findClazzByClazzName(String name);

    int saveClazz(Clazz clazz);

    List<Clazz> findList(Map<String, Object> queryConditions);

    int getTotal(String name);

    int editClazz(Clazz clazz);

    int deleteByIds(Integer[] ids);

    boolean gradeIsAir(Integer[] ids);
    List<Clazz> findAll();

}

package com.ljs.ssm_student.service.impl;

import com.ljs.ssm_student.dao.ClazzDAO;
import com.ljs.ssm_student.entity.Clazz;
import com.ljs.ssm_student.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzDAO clazzDAO;

    @Override
    public Clazz findClazzByClazzName(String name) {
        return clazzDAO.findClazzByClazzName(name);
    }

    @Override
    public int saveClazz(Clazz clazz) {
        return clazzDAO.saveClazz(clazz);
    }

    @Override
    public List<Clazz> findList(Map<String, Object> queryConditions) {
        return clazzDAO.findList(queryConditions);
    }

    @Override
    public int getTotal(String name) {
        return clazzDAO.getTotal(name);
    }

    @Override
    public int editClazz(Clazz clazz) {
        return clazzDAO.editClazz(clazz);
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        return clazzDAO.deleteByIds(ids);
    }

    @Override
    public boolean gradeIsAir(Integer[] ids) {
        int i = clazzDAO.gradeIsAir(ids);
        return i <= 0;
    }

    @Override
    public List<Clazz> findAll() {
        return clazzDAO.findAll();
    }
}

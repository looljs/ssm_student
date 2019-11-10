package com.ljs.ssm_student.service.impl;

import com.ljs.ssm_student.dao.UserDAO;
import com.ljs.ssm_student.entity.User;
import com.ljs.ssm_student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User findUserByUserName(String username) {
        return userDAO.findUserByUserName(username);
    }

    @Override
    public int saveUser(User user) {
        return userDAO.saveUser(user);
    }

    @Override
    public List<User> findList(Map<String, Object> queryConditions) {
        return userDAO.findList(queryConditions);
    }

    @Override
    public int getTotal(String username) {
        return userDAO.getTotal(username);
    }

    @Override
    public int editUser(User user) {
        return userDAO.editUser(user);
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        return userDAO.deleteByIds(ids);
    }
}

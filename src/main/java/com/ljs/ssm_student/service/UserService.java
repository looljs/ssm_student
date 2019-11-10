package com.ljs.ssm_student.service;

import com.ljs.ssm_student.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findUserByUserName(String username);
    int saveUser(User user);

    List<User> findList(Map<String, Object> queryConditions);

    int getTotal(String username);

    int editUser(User user);

    int deleteByIds(Integer[] ids);
}

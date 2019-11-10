package com.ljs.ssm_student.utils;

import com.ljs.ssm_student.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PrimaryKeyGenerationUtil {

    private String Prefix = "192168";

    @Autowired
    private UserDAO userDAO;

    public String getId(){
        String id = userDAO.getId() + 1 + "";
        String value = String.valueOf(new Random().nextInt());
        value = value.replace("-","");
        return Prefix + id + value.substring(0,12-6-id.length());
    }
}

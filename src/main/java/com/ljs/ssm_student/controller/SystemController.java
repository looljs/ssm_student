package com.ljs.ssm_student.controller;

import com.ljs.ssm_student.entity.Student;
import com.ljs.ssm_student.entity.User;
import com.ljs.ssm_student.service.StudentService;
import com.ljs.ssm_student.service.UserService;
import com.ljs.ssm_student.utils.CpachaUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统主页控制器
 */

@Controller
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(){
        return "index";
    }



    /**
     * 表单验证功能
     * @param username
     * @param password
     * @param vCode
     * @param type
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> loginVerification(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "password", required = true) String password,
            @RequestParam(name = "vcode", required = true) String vCode,
            @RequestParam(name = "type", required = true) int type,
            HttpServletRequest request
    ){
        Map map = new HashMap<String,String>();

        if (StringUtils.isEmpty(username)){
            map.put("type","error");
            map.put("msg","用户名为空");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("type","error");
            map.put("msg","密码为空");
            return map;
        }
        if (StringUtils.isEmpty(vCode)){
            map.put("type","error");
            map.put("msg","验证码为空");
            return map;
        }
        if (!StringUtils.upperCase((String) request.getSession().getAttribute("VCode")).equals(StringUtils.upperCase(vCode))){
            map.put("type","error");
            map.put("msg","验证码错误");
            return map;
        }

        if (type == 1){
            User user = userService.findUserByUserName(username);
            if (user == null){
                map.put("type","error");
                map.put("msg","用户名或密码错误");
                return map;
            }
            if (!StringUtils.equals(password,user.getPassword())){
                map.put("type","error");
                map.put("msg","用户名或密码错误");
                return map;
            }
            request.getSession().setAttribute("userType",type);
            map.put("type","success");
            map.put("msg","登陆成功");
            request.getSession().setAttribute("user",user);
            request.getSession().setAttribute("name",user.getUsername());
            return map;
        }else {
            Student student = studentService.findStudentByStudentName(username);
            if (student == null){
                map.put("type","error");
                map.put("msg","用户名或密码错误");
                return map;
            }
            if (!StringUtils.equals(password,student.getPassword())){
                map.put("type","error");
                map.put("msg","用户名或密码错误");
                return map;
            }
            map.put("type","success");
            map.put("msg","登陆成功");
            request.getSession().setAttribute("userType",type);
            request.getSession().setAttribute("user",student);
            request.getSession().setAttribute("name",student.getUsername());
            return map;
        }
    }

    @RequestMapping(value = "/get_verification_code",method = RequestMethod.GET)
    public void getVerificationCode(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        CpachaUtil cpachaUtil = new CpachaUtil(4,160,40);
        String generatorVCode = cpachaUtil.generatorVCode();
        httpRequest.getSession().setAttribute("VCode",generatorVCode);
        BufferedImage bufferedImage = cpachaUtil.generatorVCodeImage(generatorVCode, true);
        try {
            ImageIO.write(bufferedImage,"gif",httpResponse.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpServletRequest httpRequest){
        httpRequest.getSession().removeAttribute("userType");
        httpRequest.getSession().removeAttribute("user");
        httpRequest.getSession().removeAttribute("name");
        return "login";
    }


}

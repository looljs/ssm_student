package com.ljs.ssm_student.controller;

import com.ljs.ssm_student.entity.User;
import com.ljs.ssm_student.page.Page;
import com.ljs.ssm_student.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(){
        return "user/list";
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> edit(User user){
        Map<String, String> map = new HashMap<>();
        if (user == null){
            map.put("type","error");
            map.put("meg","数据绑定出错,请联系作者");
            return map;
        }
        if (StringUtils.isEmpty(user.getUsername())){
            map.put("type","error");
            map.put("meg","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(user.getUsername())){
            map.put("type","password");
            map.put("meg","密码不能为空");
            return map;
        }
        User userName = userService.findUserByUserName(user.getUsername());
        if (userName != null && user.getId() != userName.getId()){
            map.put("type","error");
            map.put("meg","用户名已存在");
            return map;
        }
        int status = userService.editUser(user);
        if (status <= 0){
            map.put("type","error");
            map.put("meg","修改用户失败");
            return map;
        }
        map.put("type","success");
        map.put("meg","修改成功");
        return map;
    }
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> add(User user){
        Map<String, String> map = new HashMap<>();
        if (user == null){
            map.put("type","error");
            map.put("meg","数据绑定出错,请联系作者");
            return map;
        }
        if (StringUtils.isEmpty(user.getUsername())){
            map.put("type","error");
            map.put("meg","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(user.getUsername())){
            map.put("type","password");
            map.put("meg","密码不能为空");
            return map;
        }
        User userName = userService.findUserByUserName(user.getUsername());
        if (userName != null){
            map.put("type","error");
            map.put("meg","用户名已存在");
            return map;
        }
        int status = userService.saveUser(user);
        if (status <= 0){
            map.put("type","error");
            map.put("meg","添加用户失败");
            return map;
        }
        map.put("type","success");
        map.put("meg","添加成功");
        return map;
    }

    @RequestMapping(value = "get_list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findList(
            @RequestParam(value = "username",defaultValue = "") String username,
            Page page
    ){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> queryConditions = new HashMap<>();
        queryConditions.put("username","%"+username+"%");
        queryConditions.put("start",page.getStart());
        queryConditions.put("size",page.getRows());
        map.put("rows",userService.findList(queryConditions));
        map.put("total", userService.getTotal("%"+username+"%"));
        return map;
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> delete(@RequestParam("ids[]") Integer[] ids){
        Map<String,String> map = new HashMap<>();
        if (ids == null){
            map.put("type","error");
            map.put("msg","请选择要删除的数据");
            return map;
        }
        int rows = userService.deleteByIds(ids);
        if (rows<=0){
            map.put("type","error");
            map.put("msg","删除失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","删除"+rows+"条数据成功");
        return map;
    }

}

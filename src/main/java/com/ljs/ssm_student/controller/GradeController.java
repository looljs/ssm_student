package com.ljs.ssm_student.controller;

import com.ljs.ssm_student.entity.Grade;
import com.ljs.ssm_student.page.Page;
import com.ljs.ssm_student.service.GradeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/grade")
public class    GradeController {

    @Autowired
    private GradeService gradeService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView list(ModelAndView mv){
        mv.setViewName("grade/list");
        return mv;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> edit(Grade grade){
        Map<String, String> map = new HashMap<>();
        if (grade == null){
            map.put("type","error");
            map.put("meg","数据绑定出错,请联系作者");
            return map;
        }
        if (StringUtils.isEmpty(grade.getName())){
            map.put("type","error");
            map.put("meg","年级名不能为空");
            return map;
        }
        Grade gradeByName= gradeService.findGradeByName(grade.getName());
        if (gradeByName != null && gradeByName.getId() != grade.getId()){
            map.put("type","error");
            map.put("meg","年级名已存在");
            return map;
        }
        int status = gradeService.editUser(grade);
        if (status <= 0){
            map.put("type","error");
            map.put("meg","修改年级失败");
            return map;
        }
        map.put("type","success");
        map.put("meg","修改成功");
        return map;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> add(Grade grade){
        Map<String, String> map = new HashMap<>();
        if (grade == null){
            map.put("type","error");
            map.put("msg","数据绑定出错,请联系作者");
            return map;
        }
        if (StringUtils.isEmpty(grade.getName())){
            map.put("type","error");
            map.put("msg","班级名不能为空");
            return map;
        }
        Grade gradeByName = gradeService.findGradeByName(grade.getName());
        if (gradeByName != null){
            map.put("type","error");
            map.put("msg","年级已存在");
            return map;
        }
        int status = gradeService.saveGrade(grade);
        if (status <= 0){
            map.put("type","error");
            map.put("msg","添加年级失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","添加成功");
        return map;
    }

    @RequestMapping(value = "get_list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findList(
            @RequestParam(value = "name",defaultValue = "") String name,
            Page page
    ){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> queryConditions = new HashMap<>();
        queryConditions.put("name","%"+name+"%");
        queryConditions.put("start",page.getStart());
        queryConditions.put("size",page.getRows());
        map.put("rows",gradeService.findList(queryConditions));
        map.put("total", gradeService.getTotal("%"+name+"%"));
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
        //判断要删除的年级下受否有班级
        if(!gradeService.gradeIsAir(ids)){
            map.put("type","error");
            map.put("msg","年级下包含班级，无法删除");
            return map;
        }
        int rows = gradeService.deleteByIds(ids);
        if (rows<=0){
            map.put("type","error");
            map.put("msg","删除失败");
            return map;
        }
        map.put("type","success");
//        map.put("msg","删除"+rows+"条数据成功");
        return map;
    }
}

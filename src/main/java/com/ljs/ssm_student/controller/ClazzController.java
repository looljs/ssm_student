package com.ljs.ssm_student.controller;

import com.ljs.ssm_student.entity.Clazz;
import com.ljs.ssm_student.page.Page;
import com.ljs.ssm_student.service.ClazzService;
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
@RequestMapping("/clazz")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private GradeService gradeService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView list(ModelAndView mv){
        mv.setViewName("clazz/list");
        mv.addObject("gradeList",gradeService.findAll());
        return mv;
    }

    @RequestMapping(value = "get_list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findList(
            @RequestParam(value = "name",defaultValue = "") String name,
            @RequestParam(value = "gradeId",defaultValue = "") String gradeId,
            Page page
    ){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> queryConditions = new HashMap<>();
        //去除空格的影响
        if (!StringUtils.isEmpty(name)){
            name = name.trim();
        }
        queryConditions.put("name","%"+name+"%");
        if (gradeId != null && !StringUtils.isEmpty(gradeId)){
            queryConditions.put("gradeId",gradeId);
        }else {
            queryConditions.put("gradeId",null);
        }
        queryConditions.put("start",page.getStart());
        queryConditions.put("size",page.getRows());
        map.put("rows",clazzService.findList(queryConditions));
        map.put("total", clazzService.getTotal("%"+name+"%"));
        return map;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> edit(Clazz clazz){
        Map<String, String> map = new HashMap<>();
        if (clazz == null){
            map.put("type","error");
            map.put("meg","数据绑定出错,请联系作者");
            return map;
        }
        if (StringUtils.isEmpty(clazz.getName())){
            map.put("type","error");
            map.put("meg","班级名不能为空");
            return map;
        }
        Clazz clazz1 = clazzService.findClazzByClazzName(clazz.getName());
        if (clazz1 != null && clazz1.getId() != clazz.getId()){
            map.put("type","error");
            map.put("meg","班级名已存在");
            return map;
        }
        int status = clazzService.editClazz(clazz);
        if (status <= 0){
            map.put("type","error");
            map.put("meg","修改班级失败");
            return map;
        }
        map.put("type","success");
        map.put("meg","修改成功");
        return map;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> add(Clazz clazz){
        Map<String, String> map = new HashMap<>();
        if (clazz == null){
            map.put("type","error");
            map.put("msg","数据绑定出错,请联系作者");
            return map;
        }
        if (StringUtils.isEmpty(clazz.getName())){
            map.put("type","error");
            map.put("msg","班级名不能为空");
            return map;
        }
        Clazz clazz1 = clazzService.findClazzByClazzName(clazz.getName());
        if (clazz1 != null){
            map.put("type","error");
            map.put("msg","班级已存在");
            return map;
        }
        int status = clazzService.saveClazz(clazz);
        if (status <= 0){
            map.put("type","error");
            map.put("msg","添加班级失败");
            return map;
        }
        map.put("type","success");
        map.put("msg","添加成功");
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
        if(!clazzService.gradeIsAir(ids)){
            map.put("type","error");
            map.put("msg","班级下包含学生，无法删除");
            return map;
        }
        int rows = clazzService.deleteByIds(ids);
        if (rows<=0){
            map.put("type","error");
            map.put("msg","删除失败");
            return map;
        }
        map.put("type","success");
        return map;
    }

    @RequestMapping(value = "names",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> names(){
        Map<String,Object> map = new HashMap<>();
        map.put("type","success");
        map.put("gradeList",gradeService.findAll());
        return map;
    }
}

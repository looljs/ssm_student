package com.ljs.ssm_student.controller;

import com.ljs.ssm_student.entity.Student;
import com.ljs.ssm_student.page.Page;
import com.ljs.ssm_student.service.ClazzService;
import com.ljs.ssm_student.service.StudentService;
import com.ljs.ssm_student.utils.PrimaryKeyGenerationUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private PrimaryKeyGenerationUtil primaryKeyGenerationUtil;
    @Autowired
    private ClazzService clazzService;

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView list(ModelAndView mv){
        mv.setViewName("student/list");
        mv.addObject("clazzList",clazzService.findAll());
        return mv;
    }

    @RequestMapping(value = "get_list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findList(
            @RequestParam(value = "username",defaultValue = "") String username,
            @RequestParam(value = "clazzId",defaultValue = "") String clazzId,
            Page page,
            HttpServletRequest request
    ){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> queryConditions = new HashMap<>();
        //去除空格的影响
        if (!StringUtils.isEmpty(username)){
            username = username.trim();
        }
        queryConditions.put("username","%"+username+"%");
        if (clazzId != null && !StringUtils.isEmpty(clazzId)){
            queryConditions.put("clazzId",clazzId);
        }else {
            queryConditions.put("clazzId",null);
        }
        queryConditions.put("start",page.getStart());
        queryConditions.put("size",page.getRows());
//        if (StringUtils.equals("2",))){
//            map.put("rows",new ArrayList<>().add(studentService.findStudentByStudentName((String) request.getSession().getAttribute("name"))));
//            map.put("total", studentService.getTotal("%"+username+"%"));
//            return map;
//        }
        if (String.valueOf(request.getSession().getAttribute("userType")).equals("2")){
            Student student = studentService.findStudentByStudentName((String) request.getSession().getAttribute("name"));
            List<Student> students = new ArrayList<>();
            students.add(student);
            map.put("rows",students);
            map.put("total", 1);
            return map;
        }
        map.put("rows",studentService.findList(queryConditions));
        map.put("total", studentService.getTotal("%"+username+"%"));
        return map;
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> edit(Student student){
        Map<String, String> map = new HashMap<>();
        if (student == null){
            map.put("type","error");
            map.put("meg","数据绑定出错,请联系作者");
            return map;
        }
        if (StringUtils.isEmpty(student.getUsername())){
            map.put("type","error");
            map.put("meg","学生名不能为空");
            return map;
        }
        Student student1 = studentService.findStudentByStudentName(student.getUsername());
        if (student1 != null && student1.getId() != student.getId()){
            map.put("type","error");
            map.put("meg","学生名已存在");
            return map;
        }
        int status = studentService.editStudent(student);
        if (status <= 0){
            map.put("type","error");
            map.put("meg","修改学生失败");
            return map;
        }
        map.put("type","success");
        map.put("meg","修改成功");
        return map;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> add(Student student){
        Map<String, String> map = new HashMap<>();
        if (student == null){
            map.put("type","error");
            map.put("msg","数据绑定出错,请联系作者");
            return map;
        }
        if (StringUtils.isEmpty(student.getUsername())){
            map.put("type","error");
            map.put("msg","学生名不能为空");
            return map;
        }
        Student student1 = studentService.findStudentByStudentName(student.getUsername());
        if (student1 != null){
            map.put("type","error");
            map.put("msg","学生已存在");
            return map;
        }
        student.setSn(primaryKeyGenerationUtil.getId());
        int status = studentService.saveStudent(student);
        if (status <= 0){
            map.put("type","error");
            map.put("msg","添加学生失败");
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
        int rows = studentService.deleteByIds(ids);
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
        map.put("clazzList",clazzService.findAll());
        return map;
    }

    @RequestMapping(value = "upload_photo",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadPhoto(@RequestParam("photo") MultipartFile file,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<>();
        if (file == null){
            map.put("type","error");
            map.put("msg","上传内容不能为空");
            return map;
        }
        //判断文件大小
        if (file.getSize() > 10485760){
            map.put("type","error");
            map.put("msg","图片过大，上传失败");
            return map;
        }
        //获取文件后缀名
        String s = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
        if (!StringUtils.containsAny(s.toLowerCase(),"jpg","png","gif")){
            map.put("type","error");
            map.put("msg","文件格式不正确");
            return map;
        }
        //判断图片内容是否正常
        BufferedImage read = null;
        try {
            read = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            map.put("type","error");
            map.put("msg","文件上传失败");
            return map;
        }
        if (read == null){
            map.put("type","error");
            map.put("msg","文件内容不支持");
            return map;
        }
        String savePath = request.getSession().getServletContext().getRealPath("/") + "uploadfile";
        File savePathFile = new File(savePath);
        if(!savePathFile.exists()){
            savePathFile.mkdir();//如果不存在，则创建一个文件夹upload
        }
        //返回客户端文件系统中的原始文件名
        String originalFilename = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString()+
                originalFilename.substring(
                        originalFilename.lastIndexOf('.'),
                        originalFilename.length());
        file.transferTo(new File(savePathFile,newName));
        map.put("type","success");
        map.put("src", request.getSession().getServletContext().getContextPath() + "/uploadfile/" + newName);
        map.put("msg","上传成功");
        return map;
    }
}
package com.mr.web;

import com.mr.pojo.StudentEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/15 10:56
 */
@Controller
public class TestController {

    /**
     * 展示学生
     * @param map
     * @return
     */
    @GetMapping(value = "test")
    public String test(ModelMap map){
     /*   map.put("name","tomcat");
        return "test";*/
        StudentEntity student = new StudentEntity();
        student.setCode("有始就有终");
        student.setPass("110");
        student.setAge(15);
        student.setLikeColor("<font color='red'>刷刷水啦</font>");

        StudentEntity s1=new StudentEntity("001","111",18,"red");
        StudentEntity s2=new StudentEntity("002","222",19,"red");
        StudentEntity s3=new StudentEntity("003","333",16,"blue");
        StudentEntity s4=new StudentEntity("004","444",28,"blue");
        StudentEntity s5=new StudentEntity("005","555",68,"blue");
        //转为List
        map.put("stuList", Arrays.asList(s1,s2,s3,s4,s5));

        map.put("stu",student);
        return  "test";

    }

}

package com.mr.pojo;

import lombok.Data;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/15 10:50
 */
@Data
public class StudentEntity{

    String code;
    String pass;
    int age;
    String likeColor;

    public StudentEntity(String code, String pass, int age, String likeColor) {
        this.code = code;
        this.pass = pass;
        this.age = age;
        this.likeColor = likeColor;
    }

    public StudentEntity() {
    }
}

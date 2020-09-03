package com.baidu.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/3 11:43
 */
@Table(name = "tb_spec_group")
@Data
public class SpecGroupEntity {

    @Id
    private  Integer id;

    private  Integer cid;

    private  String name;
}

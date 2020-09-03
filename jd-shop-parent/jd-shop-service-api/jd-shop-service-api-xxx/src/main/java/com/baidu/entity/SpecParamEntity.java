package com.baidu.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/3 17:40
 */
@Table(name = "tb_spec_param")
@Data
public class SpecParamEntity {

    @Id
    private Integer id;

    private Integer cid;

    private Integer groupId;

    private String name;

    //numeric是mysql数据库的关键字,
    //SELECT  id,cid,group_id,name,numeric,unit,generic,searching,segments  FROM tb_spec_param  WHERE (       (  group_id = 1 ) )
    //这句sql不会执行错误
    //加上``会当成普通字符串处理
    @Column(name = "`numeric`")
    private Boolean numeric;

    private String unit;

    private Boolean generic;

    private Boolean  searching;

    private String segments;

}

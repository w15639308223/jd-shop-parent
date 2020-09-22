package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/8 19:11
 */
@Table(name = "tb_spu_detail")
@Data
public class SpuDetailEntity {

    @Id
    private Integer spuId;

    private String description;

    private String genericSpec;

    private String specialSpec;

    private String packingList;

    private String afterService;
}


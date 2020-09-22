package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Table;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/1 14:46
 */
@Table(name = "tb_category_brand")
@Data
public class CategoryBrandEntity {

    private  Integer categoryId;

    private  Integer brandId;

}

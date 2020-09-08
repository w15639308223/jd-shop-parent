package com.baidu.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/7 14:49
 */
@Table(name = "tb_spu")
@Data
public class SpuEntity {

    @Id
    private Integer id;

    private String title;

    private String subTitle;

    private Integer cid1;

    private Integer cid2;

    private Integer cid3;

    private Integer brandId;

    private Integer saleable;

    private Integer valid;

    private Date createTime;

    private Date lastUpdateTime;

}

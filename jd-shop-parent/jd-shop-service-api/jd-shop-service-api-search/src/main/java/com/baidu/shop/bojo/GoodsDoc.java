package com.baidu.shop.bojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/16 20:35
 */
@Document(indexName = "goods",shards = 1,replicas = 0)
@Data
public class GoodsDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String brandName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String categoryName;

    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;

    private Long brandId;

    private Long cid1;

    private Long cid2;

    private Long cid3;

    private Date createTime;

    private List<Long> price;

    @Field(type = FieldType.Keyword, index = false)
    private String skus;

    //规格
    private Map<String, Object> specs;

}

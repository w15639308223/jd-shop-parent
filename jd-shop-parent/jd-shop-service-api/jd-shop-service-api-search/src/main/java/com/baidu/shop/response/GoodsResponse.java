package com.baidu.shop.response;

import com.baidu.shop.base.Result;
import com.baidu.shop.bojo.GoodsDoc;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.status.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/21 14:42
 */
@Data
@NoArgsConstructor
public class GoodsResponse extends Result<List<GoodsDoc>> {

    private Long total;

    private Long totalPage;

    private List<BrandEntity> brandList;

    private List<CategoryEntity> categoryList;

    private Map<String,List<String>> specParamMap;

    public GoodsResponse(Long total, Long totalPage, List<BrandEntity> brandList,
                         List<CategoryEntity> categoryList,List<GoodsDoc> goodsDocs,
                         Map<String,List<String>> specParamMap) {
        super(HttpStatus.OK,HttpStatus.OK + "",goodsDocs);
        this.total = total;
        this.totalPage = totalPage;
        this.brandList = brandList;
        this.categoryList = categoryList;
        this.specParamMap = specParamMap;
    }

}

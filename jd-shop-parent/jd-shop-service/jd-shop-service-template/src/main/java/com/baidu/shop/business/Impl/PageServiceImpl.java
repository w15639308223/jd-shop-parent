package com.baidu.shop.business.Impl;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.*;
import com.baidu.shop.entity.*;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.github.pagehelper.PageInfo;
import com.baidu.shop.feign.CategoryFeign;
import com.baidu.shop.business.PageService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/23 20:57
 */
//@Service
public class PageServiceImpl implements PageService {

    //@Autowired
    private BrandFeign brandFeign;

    //@Autowired
    private CategoryFeign categoryFeign;

    //@Autowired
    private SpecificationFeign specificationFeign;

    //@Autowired
    private GoodsFeign goodsFeign;

    @Override
    public Map<String, Object> getPageInfoBySpuId(Integer spuId) {

        Map<String, Object> map = new HashMap<>();
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setId(spuId);
        Result<List<SpuDTO>> spuInfoResult = goodsFeign.getSpuInfo(spuDTO);
        if (spuInfoResult.getCode() == 200) {

            if (spuInfoResult.getData().size() == 1) {
                //spu信息
                SpuDTO spuInfo = spuInfoResult.getData().get(0);
                map.put("spuInfo",spuInfo);
                //品牌信息
                BrandDTO brandDTO = new BrandDTO();
                brandDTO.setId(spuInfo.getBrandId());
                Result<PageInfo<BrandEntity>> brandInfoResult = brandFeign.getBrandInfo(brandDTO);
                if(brandInfoResult.getCode() == 200){
                    PageInfo<BrandEntity> data = brandInfoResult.getData();

                    List<BrandEntity> brandList = data.getList();
                    if(brandList.size() == 1){
                        map.put("brandInfo",brandList.get(0));
                    }
                }

                //分类信息
                Result<List<CategoryEntity>> categoryResult = categoryFeign.getCategoryByIdList(String.join(",",Arrays.asList(spuInfo.getCid1() + "", spuInfo.getCid2() + "", spuInfo.getCid3() + "")));
                if(categoryResult.getCode() == 200){
                    List<CategoryEntity> categoryEntityList = categoryResult.getData();
                    map.put("categoryList",categoryEntityList);
                }

                //skus
                //通过spuID查询sku集合
                Result<List<SkuDTO>> skusResult = goodsFeign.getSkuBySpu(spuInfo.getId());
                if (skusResult.getCode() == 200) {
                    List<SkuDTO> skuList = skusResult.getData();
                    map.put("skus",skuList);
                }

                //特有规格参数
                SpecParamDTO specParamDTO = new SpecParamDTO();
                specParamDTO.setCid(spuInfo.getCid3());
                specParamDTO.setGeneric(false);
                Result<List<SpecParamEntity>> specParamInfoResult = specificationFeign.getSpecParamInfo(specParamDTO);
                if (specParamInfoResult.getCode() == 200) {
                    List<SpecParamEntity> specParamList = specParamInfoResult.getData();

                    Map<Integer, String> specParamMap = new HashMap<>();
                    specParamList.stream().forEach(param -> {
                        specParamMap.put(param.getId(),param.getName());
                    });

                    map.put("specParamMap",specParamMap);
                }

                //spuDetail详情
                Result<SpuDetailEntity> detailResult = goodsFeign.getSpuDetailByIdSpu(spuInfo.getId());
                if(detailResult.getCode() == 200){
                    SpuDetailEntity spuDetailEntity = detailResult.getData();
                    map.put("spuDetailEntity",spuDetailEntity);
                }
                //规格组和规格参数
                SpecGroupDTO specGroupDTO = new SpecGroupDTO();
                specGroupDTO.setCid(spuDTO.getCid3());
                Result<List<SpecGroupEntity>> sepcGroupResult = specificationFeign.getSpecGroupInfo(specGroupDTO);
                if (sepcGroupResult.getCode() ==200){
                    List<SpecGroupEntity> specGroupInfo = sepcGroupResult.getData();
                    //规格组和规格参数
                    List<SpecGroupDTO> specGroupDTOList = specGroupInfo.stream().map(specGroupEntity -> {
                        SpecGroupDTO specGroup = BaiduBeanUtil.copyProperties(specGroupEntity, SpecGroupDTO.class);
                        //规格参数 通用参数
                        SpecParamDTO paramDTO = new SpecParamDTO();
                        paramDTO.setGroupId(specGroup.getId());
                        //sku通用属性不能为空
                        paramDTO.setGeneric(true);
                        Result<List<SpecParamEntity>> specParamResult = specificationFeign.getSpecParamInfo(paramDTO);
                        if (specParamResult.getCode() == 200) {
                            specGroup.setParamList(specParamResult.getData());
                        }
                        return specGroup;
                    }).collect(Collectors.toList());
                    map.put("specGroupDTOList",specGroupDTOList);
                }
            }
        }
        return map;
    }
}

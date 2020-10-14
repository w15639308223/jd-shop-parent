package com.baidu.shop.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.*;
import com.baidu.shop.entity.*;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.CategoryFeign;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.service.TemplateService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/25 19:11
 */
@RestController
public class TemplateServiceImpl extends BaseApiService implements TemplateService {
    @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SpecificationFeign specificationFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Value(value = "${mrshop.static.html.path}")
    private String staticHtmlPath;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public Result<JsonObject> createStaticHtmlTemplate(Integer spuId) {

        Map<String, Object> map = this.getPageInfoBySpuId(spuId);
        //创建模板引擎上下文
        Context context = new Context();
        //将所有准备的数据放到模板中
        context.setVariables(map);
        //文件存放地址
        File file = new File(staticHtmlPath, spuId+".html");
        //构建文件输出流
        PrintWriter writer = null;
        //模板引擎   模板名称 上下文  输出流
        try {
            writer = new PrintWriter(file, "UTF-8");
            templateEngine.process("item",context,writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {
            //关流(很重要)
            writer.close();
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> insertStaicHtmlTemplate() {
        //先获取所有spu数据
        Result<List<SpuDTO>> spuInfoResult = goodsFeign.getSpuInfo(new SpuDTO());
        if (spuInfoResult.getCode() ==200) {
            List<SpuDTO> spuDTOList = spuInfoResult.getData();
            spuDTOList.stream().forEach(spuDTO -> {
               createStaticHtmlTemplate(spuDTO.getId());
            });
        }
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> delHTMLBySpuId(Integer spuId) {
        File file = new File(staticHtmlPath + File.separator + spuId + ".html");

        if(!file.delete()){
            return this.setResultError("文件删除失败");
        }

        return this.setResultSuccess();
    }

   /* @Override
    public Result<JSONObject> delHTMLBySpuId(Integer spuId) {
        File file = new File(staticHTMLPath + File.separator + spuId + ".html");

        if(!file.delete()){
            return this.setResultError("文件删除失败");
        }

        return this.setResultSuccess();
    }*/

    //静态化需要的数据和值
    private Map<String, Object> getPageInfoBySpuId(Integer spuId) {
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
                Result<List<CategoryEntity>> categoryResult = categoryFeign.getCategoryByIdList(String.join(",", Arrays.asList(spuInfo.getCid1() + "", spuInfo.getCid2() + "", spuInfo.getCid3() + "")));
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

package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/25 15:34
 */
@Api(tags = "模板接口")
public interface TemplateService {

    @ApiOperation(value = "通过spuid生成静态页面")
    @GetMapping(value = "template/createStaticHtmlTemplate")
    Result<JsonObject> createStaticHtmlTemplate(Integer spuId);

    @ApiOperation(value = "初始化静态页面HTML文件")
    @GetMapping(value = "template/insertStaicHtmlTemplate")
    Result<JsonObject> insertStaicHtmlTemplate();

    @DeleteMapping(value = "template/delHTMLBySpuId")
    Result<JSONObject> delHTMLBySpuId(Integer spuId);

}

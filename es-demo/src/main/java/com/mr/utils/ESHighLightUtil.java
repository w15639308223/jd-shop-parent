package com.mr.utils;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/16 14:14
 */
@Component
public class ESHighLightUtil <T>{

    //构建高亮字段buiilder
    //highLightField 里边存放的一些title   brand category ...
    public static HighlightBuilder getHighlightBuilder(String ...highLightField){

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //遍历字段的集合
        Arrays.asList(highLightField).forEach(hlf -> {
            HighlightBuilder.Field field = new HighlightBuilder.Field(hlf);

            field.preTags("<span style=color:red'>");
            field.postTags("</span>");

            highlightBuilder.field(field);
        });

        return highlightBuilder;
    }

    //将返回的内容替换成高亮
    public static <T> List<SearchHit<T>> getHighLightHit(List<SearchHit<T>> list){

        return list.stream().map(hit -> {
            //得到高亮字段
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            //遍历 key 定义的高亮的字段   value是list集合高亮之后的字段
            highlightFields.forEach((key,value) -> {
                try {
                    //得到传递参数的类型
                    T content = hit.getContent();//当前文档 T为当前文档类型

                    //content.getClass()获取当前文档类型,并且得到排序字段的set方法        第一个字符转换为大写                      获得高亮的字段        参数的类型
                    Method method = content.getClass().getMethod("set" + String.valueOf(key.charAt(0)).toUpperCase() + key.substring(1),String.class);

                    //执行set方法并赋值
                    method.invoke(content,value.get(0));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            });

            return hit;
        }).collect(Collectors.toList());

    }

}

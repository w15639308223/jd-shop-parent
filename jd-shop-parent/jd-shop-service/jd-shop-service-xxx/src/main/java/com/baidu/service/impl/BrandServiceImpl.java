package com.baidu.service.impl;

import com.baidu.base.BaseApiService;
import com.baidu.base.Result;
import com.baidu.dto.BrandDTO;
import com.baidu.entity.BrandEntity;
import com.baidu.entity.CategoryBrandEntity;
import com.baidu.mapper.BrandMapper;
import com.baidu.mapper.CategoryBrandMapper;
import com.baidu.service.BrandService;
import com.baidu.utils.BaiduBeanUtil;
import com.baidu.utils.ObjectUtil;
import com.baidu.utils.PinyinUtil;
import com.baidu.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/8/31 15:30
 */
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Transactional
    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {

        //分页
        if (ObjectUtil.isNOtNull(brandDTO.getPage()) && ObjectUtil.isNOtNull(brandDTO.getRows()))
            PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());

        //排序 & 条件查询
        Example example = new Example(BrandEntity.class);
        if (StringUtil.isNotEmpty(brandDTO.getSort())) example.setOrderByClause(brandDTO.getOrderByClause());

        Example.Criteria criteria = example.createCriteria();
        if (ObjectUtil.isNOtNull(brandDTO.getId()))
            criteria.andEqualTo("id",brandDTO.getId());

        //模糊查询
        if (StringUtil.isNotEmpty(brandDTO.getName())) criteria
                .andLike("name","%" + brandDTO.getName() + "%");

        //查询
        List<BrandEntity> list = brandMapper.selectByExample(example);
        //前端页面数据封装
        PageInfo<BrandEntity> PageInfo = new PageInfo<>(list);

        return this.setResultSuccess(PageInfo);
    }

    @Transactional
    @Override
    public Result<JsonObject> saveBrandInfo(BrandDTO brandDTO) {

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        //获取到品牌名称
        //获取到品牌名称第一个字符
        //将第一个字符转换为pinyin
        //获取拼音的首字母
        //统一转为大写

/*        String name = brandEntity.getName();
        char c = name.charAt(0);
        String upperCase = PinyinUtil.getUpperCase(String.valueOf(c), PinyinUtil.TO_FIRST_CHAR_PINYIN);
        brandEntity.setLetter(upperCase.charAt(0));*/

        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().charAt(0))
                , PinyinUtil.TO_FIRST_CHAR_PINYIN).charAt(0));
        brandMapper.insertSelective(brandEntity);
    //分割 得到数组, 批量新增
    //String[] cidArr = brandDTO.getCategory().split(",");

     //List<String> list = Arrays.asList(cidArr);

       /* List<CategoryBrandEntity> categoryBrandEntities = new ArrayList<>();
        list.stream().forEach(cid -> {
            CategoryBrandEntity entity = new CategoryBrandEntity();
            entity.setCategoryId(StringUtil.toInteger(cid));
            entity.setBrandId(brandEntity.getId());
            categoryBrandEntities.add(entity);
        });*/
        //增加
        this.insertCatrgoryAndBrand(brandDTO,brandEntity);

        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JsonObject> editBrand(BrandDTO brandDTO) {

        //获得品牌数据
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

        //将汉字should字母修改为大写
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().charAt(0))
                ,PinyinUtil.TO_FIRST_CHAR_PINYIN).charAt(0));
        //执行修改
        brandMapper.updateByPrimaryKeySelective(brandEntity);

        //通过brandId删除中间表的数据
        this.deleteCategoryAndBrand(brandEntity.getId());

        //增加新数据
        this.insertCatrgoryAndBrand(brandDTO,brandEntity);

        return this.setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JsonObject> delBrand(Integer id) {
        //删除品牌
        brandMapper.deleteByPrimaryKey(id);

        this.deleteCategoryAndBrand(id);
        return this.setResultSuccess();
    }

    private void  insertCatrgoryAndBrand(BrandDTO brandDTO,BrandEntity brandEntity){

        if(brandDTO.getCategory().contains(",")){
            //通过split方法分割字符串的Array
            //Arrays.asList将Array转换为List
            //使用JDK1,8的stream
            //使用map函数返回一个新的数据
            //collect 转换集合类型Stream<T>
            //Collectors.toList())将集合转换为List类型
            List<CategoryBrandEntity> CategoryBrandEntitys = Arrays.asList(brandDTO.getCategory().split(","))
                    .stream().map(cid ->{
                        CategoryBrandEntity entity = new CategoryBrandEntity();
                        entity.setCategoryId(StringUtil.toInteger(cid));
                        entity.setBrandId(brandEntity.getId());
                        return  entity;
                    }).collect(Collectors.toList());

            categoryBrandMapper.insertList(CategoryBrandEntitys);
        }else{
            CategoryBrandEntity entity = new CategoryBrandEntity();
            entity.setCategoryId(StringUtil.toInteger(brandDTO.getCategory()));
            entity.setBrandId(brandEntity.getId());

            categoryBrandMapper.insertSelective(entity);
        }
    }

    private  void  deleteCategoryAndBrand(Integer id){

        //通过brandid删除中间表的数据
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",id);
        categoryBrandMapper.deleteByExample(example);
    }
}

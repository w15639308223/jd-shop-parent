package com.baidu.service.impl;

import com.baidu.base.BaseApiService;
import com.baidu.base.Result;
import com.baidu.dto.BrandDTO;
import com.baidu.dto.SpuDTO;
import com.baidu.entity.BrandEntity;
import com.baidu.entity.SpuEntity;
import com.baidu.mapper.CategoryMapper;
import com.baidu.mapper.SpuMapper;
import com.baidu.service.BrandService;
import com.baidu.service.GoodsService;
import com.baidu.status.HttpStatus;
import com.baidu.utils.BaiduBeanUtil;
import com.baidu.utils.ObjectUtil;
import com.baidu.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 * @date: 2020/9/7 15:28
 */
@RestController
public class GoodsServiceImpl extends BaseApiService  implements GoodsService {

    @Resource
    private SpuMapper SpuMapper;

    @Resource
    private BrandService brandService;

    @Resource
    private CategoryMapper categoryMapper;

    @Transactional
    @Override
    public Result<PageInfo<SpuEntity>> getSpuInfo(SpuDTO spuDTO) {

        //分页
        if (ObjectUtil.isNOtNull(spuDTO.getPage()) && ObjectUtil.isNOtNull(spuDTO.getRows()))
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());
        //构建条件查询
        Example example = new Example(SpuEntity.class);
        //构建查询条件
        Example.Criteria criteria = example.createCriteria();
        if(StringUtil.isNotEmpty(spuDTO.getTitle()))
            criteria.andLike("title","%"+spuDTO.getTitle()+"%");
        if (ObjectUtil.isNOtNull(spuDTO.getSaleable()) && spuDTO.getSaleable() !=2)
            criteria.andEqualTo("saleable",spuDTO.getSaleable());
        //排序
        if (ObjectUtil.isNOtNull(spuDTO.getSort())) {
            example.setOrderByClause(spuDTO.getOrderByClause());
        }
        List<SpuEntity> list = SpuMapper.selectByExample(example);


        //将品牌名称和分类名称传到前端
        List<SpuDTO> spuDtoList = list.stream().map(spuEntity -> {
            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);

            //通过品牌id得到品牌名称
            //此处不需要验证参数为空,因为数据库中这个字段是必填的
            BrandDTO brandDTO = new BrandDTO();
            brandDTO.setId(spuEntity.getBrandId());
            Result<PageInfo<BrandEntity>> brandInfo = brandService.getBrandInfo(brandDTO);

            if (ObjectUtil.isNOtNull(brandInfo)) {

                PageInfo<BrandEntity> data = brandInfo.getData();
                List<BrandEntity> list1 = data.getList();

                if (!list1.isEmpty() && list1.size() == 1) {
                    spuDTO1.setBrandName(list1.get(0).getName());
                }
            }

            //设置分类
            //通过cid1 cid2 cid3

            //分类名称
            String caterogyName = categoryMapper.selectByIdList(
                    Arrays.asList(spuDTO1.getCid1(), spuDTO1.getCid2(), spuDTO1.getCid3()))
                    .stream().map(category -> category.getName())
                    .collect(Collectors.joining("/"));
            spuDTO1.setCategoryName(caterogyName);

            return spuDTO1;
        }).collect(Collectors.toList());

        /*ArrayList<SpuDTO> SpuDTOS = new ArrayList<>();
        list.stream().forEach(spuEntity -> {

            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);

            //通过品牌id查询品牌名称
            BrandDTO brandDTO = new BrandDTO();
            brandDTO.setId(spuEntity.getBrandId());

            Result<PageInfo<BrandEntity>> brandInfo = brandService.getBrandInfo(brandDTO);
            if (ObjectUtil.isNOtNull(brandInfo)){
                PageInfo<BrandEntity> data = brandInfo.getData();
                //得到品牌的集合 但是只能查询出一条数据品牌
                List<BrandEntity> list1 = data.getList();
                //获得品牌的名称
                if(!list1.isEmpty() && list1.size() ==1){
                    spuDTO1.setBrandName(list1.get(0).getName());
                }
            }
        });*/
        PageInfo<SpuEntity> pageInfo = new PageInfo<>(list);

        //返回的数据是DTO  pageInfo total
     /*   Map<String, Object> map = new HashMap<>();
        map.put("list",spuDtoList);
        map.put("total",pageInfo.getTotal());*/

        return this.setResult(HttpStatus.OK,pageInfo.getTotal() + "",spuDtoList);
    }
}

package com.baidu.shop.business.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.component.MrRabbitMQ;
import com.baidu.shop.constant.MqMessageConstant;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.business.BrandService;
import com.baidu.shop.business.GoodsService;
import com.baidu.shop.status.HttpStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/7 15:28
 */
@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private BrandService brandService;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SpuDetailMapper spuDetailMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private StockMapper stockMapper;

    @Resource
    private MrRabbitMQ rabbitMQ;

    @Transactional
    @Override
    public Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO) {

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
        if (ObjectUtil.isNOtNull(spuDTO.getId())){
            criteria.andEqualTo("id",spuDTO.getId());
        }
        //排序
        if (ObjectUtil.isNOtNull(spuDTO.getSort())) {
            example.setOrderByClause(spuDTO.getOrderByClause());
        }
        List<SpuEntity> list = spuMapper.selectByExample(example);


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

    //@Transactional
    @Override
    public Result<JsonObject> saveGoods(SpuDTO spuDTO) {

        Integer spuId = this.saveGoodsInfo(spuDTO);

        rabbitMQ.send(spuId + "", MqMessageConstant.SPU_ROUT_KEY_SAVE);
        return this.setResultSuccess();
    }

    //这样就解决了transasction等到方法结束了才提交事务的问题
    @Transactional
    public Integer saveGoodsInfo(SpuDTO spuDTO){
        Date date = new Date();

        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setSaleable(1);
        spuEntity.setValid(1);
        spuEntity.setCreateTime(date);
        spuEntity.setLastUpdateTime(date);
        //新增spu
        spuMapper.insertSelective(spuEntity);

        Integer spuId = spuEntity.getId();
        //新增spudetail
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
        //取spuid当主键
        spuDetailEntity.setSpuId(spuId);
        spuDetailMapper.insertSelective(spuDetailEntity);

        //封装后的新增
        this.addSkuAndStock(spuDTO.getSkus(),spuId,date);
        return  spuEntity.getId();
    }

    @Transactional
    @Override
    public Result<JsonObject> editGoods(SpuDTO spuDTO) {

        Date date = new Date();
        //修改spu
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setLastUpdateTime(date);
        spuMapper.updateByPrimaryKeySelective(spuEntity);
        //修改spuDetail
        SpuDetailEntity spuDetailEntity1 = BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
        spuDetailMapper.updateByPrimaryKeySelective(spuDetailEntity1);

        //修改skus
            //先通过spuId查询出来要删除的Skus集合
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuDTO.getId());
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
        //得到sku集合 然后删除
        List<Long> skuList = skuEntities.stream()
                .map(sku -> sku.getId())
                .collect(Collectors.toList());
        skuMapper.deleteByIdList(skuList);

        stockMapper.deleteByIdList(skuList);
        //再新增
        this.addSkuAndStock(spuDTO.getSkus(),spuDTO.getId(),date);

        return this.setResultSuccess();
    }



    private  void  addSkuAndStock(List<SkuDTO> skus, Integer spuId, Date date){
        skus.stream().forEach(skuDTO -> {
            //新增sku
            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuId);
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);

            //新增stock
            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);
        });

    }


    @Transactional
    @Override
    public Result<SpuDetailEntity> getSpuDetailByIdSpu(Integer spuId) {
        SpuDetailEntity spuDetailEntity = spuDetailMapper.selectByPrimaryKey(spuId);
        return this.setResultSuccess(spuDetailEntity);
    }

    @Transactional
    @Override
    public Result<List<SkuDTO>> getSkuBySpu(Integer spuId) {
        List<SkuDTO>  list=skuMapper.getSkuBySpu(spuId);
        return this.setResultSuccess(list);
    }

    @Transactional
    @Override
    public Result<JsonObject> delGoods(Integer id) {

        //删除spu
        spuMapper.deleteByPrimaryKey(id);
        //删除spuDetail详情
        spuDetailMapper.deleteByPrimaryKey(id);
        //先查询 sku 获得数组
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",id);
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
        List<Long> skuIdArr = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());
        if (skuIdArr.size() > 0){//判断一下避免数据全部被删除
            //删除sku
            skuMapper.deleteByIdList(skuIdArr);
            //删除stock
            stockMapper.deleteByIdList(skuIdArr);
        }
        return this.setResultSuccess();
    }


    @Transactional
    @Override
    public Result<JsonObject> editSaleable(SpuDTO spuDTO) {
        spuMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(spuDTO,SpuEntity.class));
        return this.setResultSuccess();
    }

    @Override
    public Result<SkuEntity> getSkuBySkuId(Long skuId) {
        SkuEntity skuEntity = skuMapper.selectByPrimaryKey(skuId);
        return this.setResultSuccess(skuEntity);
    }
}

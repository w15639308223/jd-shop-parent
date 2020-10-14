package com.baidu.shop.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.bojo.GoodsDoc;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.CategoryFeign;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.response.GoodsResponse;
import com.baidu.shop.service.ShopElasticsearchService;
import com.baidu.shop.status.HttpStatus;
import com.baidu.shop.utils.EsHighLightUtil;
import com.baidu.shop.utils.JSONUtil;
import com.baidu.shop.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ShopElasticsearchServiceImpl extends BaseApiService implements ShopElasticsearchService{

    @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private SpecificationFeign specificationFeign;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    private List<GoodsDoc> esGoodsInfo(SpuDTO spuDTO) {
        //查询出来的数据是多个spu
        List<GoodsDoc> goodsDocs = new ArrayList<>();
        //查询spu信息
        // SpuDTO spuDTO = new SpuDTO();
        Result<List<SpuDTO>> spuInfo = goodsFeign.getSpuInfo(spuDTO);
        if(spuInfo.getCode() == HttpStatus.OK){

            //spu数据
            List<SpuDTO> spuList = spuInfo.getData();

            spuList.stream().forEach(spu -> {

                GoodsDoc goodsDoc = new GoodsDoc();

                //BaiduBeanUtil.copyProperties()
                goodsDoc.setId(spu.getId().longValue());
                goodsDoc.setTitle(spu.getTitle());
                goodsDoc.setSubTitle(spu.getSubTitle());
                goodsDoc.setBrandName(spu.getBrandName());
                goodsDoc.setCategoryName(spu.getCategoryName());
                goodsDoc.setBrandId(spu.getBrandId().longValue());
                goodsDoc.setCid1(spu.getCid1().longValue());
                goodsDoc.setCid2(spu.getCid2().longValue());
                goodsDoc.setCid3(spu.getCid3().longValue());
                goodsDoc.setCreateTime(spu.getCreateTime());

                //通过spuID查询skuList
                Map<List<Long>, List<Map<String, Object>>> skus = this.getSkusAndPriceList(spu.getId());

                skus.forEach((key, value) -> {
                    goodsDoc.setPrice(key);
                    goodsDoc.setSkus(JSONUtil.toJsonString(value));
                });

                //通过cid3查询规格参数
                Map<String, Object> specMap = this.getSpecMap(spu);

                goodsDoc.setSpecs(specMap);
                goodsDocs.add(goodsDoc);

            });
        }

        return goodsDocs;
    }



    private Map<List<Long>, List<Map<String, Object>>> getSkusAndPriceList(Integer spuId){

        Map<List<Long>, List<Map<String, Object>>> hashMap = new HashMap<>();

        Result<List<SkuDTO>> skuResult = goodsFeign.getSkuBySpu(spuId);
        List<Long> priceList = new ArrayList<>();
        List<Map<String, Object>> skuMap = null;

        if(skuResult.getCode() == HttpStatus.OK){

            List<SkuDTO> skuList = skuResult.getData();
            skuMap = skuList.stream().map(sku -> {
                Map<String, Object> map = new HashMap<>();

                map.put("id", sku.getId());
                map.put("title", sku.getTitle());
                map.put("images", sku.getImages());
                map.put("price", sku.getPrice());

                priceList.add(sku.getPrice().longValue());

                return map;
            }).collect(Collectors.toList());
        }
        hashMap.put(priceList,skuMap);
        return hashMap;
    }

    private Map<String, Object> getSpecMap(SpuDTO spuDTO){

        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(spuDTO.getCid3());
        Result<List<SpecParamEntity>> specParamInfo = specificationFeign.getSpecParamInfo(specParamDTO);

        Map<String, Object> specMap = new HashMap<>();

        if (specParamInfo.getCode() == HttpStatus.OK) {
            //只有规格参数的id和规格参数的名字
            List<SpecParamEntity> paramList = specParamInfo.getData();

            //通过spuid去查询spuDetail,detail里面有通用和特殊规格参数的值
            Result<SpuDetailEntity> spuDetailResult = goodsFeign.getSpuDetailByIdSpu(spuDTO.getId());
            //因为spu和spuDetail one --> one

            if(spuDetailResult.getCode() == HttpStatus.OK){
                SpuDetailEntity spuDetaiInfo = spuDetailResult.getData();

                //通用规格参数的值
                String genericSpecStr = spuDetaiInfo.getGenericSpec();
                Map<String, String> genericSpecMap = JSONUtil.toMapValueString(genericSpecStr);

                //特有规格参数的值
                String specialSpecStr = spuDetaiInfo.getSpecialSpec();
                Map<String, List<String>> specialSpecMap = JSONUtil.toMapValueStrList(specialSpecStr);

                paramList.stream().forEach(param -> {

                    if (param.getGeneric()) {

                        if(param.getNumeric() && param.getSearching()){
                            specMap.put(param.getName(), this.chooseSegment(genericSpecMap.get(param.getId() + ""),param.getSegments(),param.getUnit()));
                        }else{
                            specMap.put(param.getName(), genericSpecMap.get(param.getId() + ""));
                        }
                    } else {
                        specMap.put(param.getName(), specialSpecMap.get(param.getId() + ""));
                    }
                });
            }
        }
        return specMap;
    }

    /**
     * 把具体的值转换成区间-->不做范围查询
     * @param value
     * @param segments
     * @param unit
     * @return
     */
    private String chooseSegment(String value, String segments, String unit) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : segments.split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + unit + "以上";
                }else if(begin == 0){
                    result = segs[1] + unit + "以下";
                }else{
                    result = segment + unit;
                }
                break;
            }
        }
        return result;
    }

    @Override
    public Result<JSONObject> initGoodsEsData() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsDoc.class);
        if(!indexOperations.exists()){
            indexOperations.create();
            log.info("索引创建成功");
            indexOperations.createMapping();
            log.info("映射创建成功");
        }

        //批量新增数据
        List<GoodsDoc> goodsDocs = this.esGoodsInfo(new SpuDTO());
        elasticsearchRestTemplate.save(goodsDocs);

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> clearGoodsEsData() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsDoc.class);
        if(indexOperations.exists()){
            indexOperations.delete();
            log.info("索引删除成功");
        }

        return this.setResultSuccess();
    }

   @Override
    public GoodsResponse search(String search,Integer page,String filter) {

        //判断
        if (StringUtil.isEmpty(search))throw new RuntimeException("搜索的内容不能为空");

        SearchHits<GoodsDoc> hits = elasticsearchRestTemplate.search(this.getSearchQueryBuilder(search,page,filter).build(), GoodsDoc.class);
        //得到的高亮字段放到content中
        List<SearchHit<GoodsDoc>> highLightHit = EsHighLightUtil.getHighLightHit(hits.getSearchHits());
        //遍历得到新的高亮字段
        List<GoodsDoc> goodsList = highLightHit.stream().map(serachHit -> serachHit.getContent()).collect(Collectors.toList());

        //总条数
        long total = hits.getTotalHits();
        //总页数
        long totalPage = Double.valueOf(Math.ceil(Long.valueOf(total).doubleValue() / 10)).longValue();
        /*Map<String, Long> messageMap = new HashMap<>();
        messageMap.put("total",total);
        messageMap.put("totalPage",totalPage);*/
        //获取聚合数据
        Aggregations aggregations = hits.getAggregations();
        //获取分类集合
       // List<CategoryEntity> cateList = this.getCateList(aggregations);
       Map<Integer, List<CategoryEntity>> map = this.getCateList(aggregations);
       List<CategoryEntity> cateList = null;
       Integer hotCid = 0;
       //遍历map集合的方式
       for (Map.Entry<Integer,List<CategoryEntity>> mapEntry:map.entrySet()){
            hotCid = mapEntry.getKey();
            cateList = mapEntry.getValue();
       }
       //通过cid查询规格参数
       Map<String, List<String>> specParamMap = this.getSpecParam(hotCid, search);

       //获取品牌集合
        List<BrandEntity> brandList = this.getBrandList(aggregations);

        GoodsResponse goodsResponse = new GoodsResponse(total, totalPage,brandList,cateList,goodsList,specParamMap);

        return goodsResponse;
    }

    @Override
    public Result<JSONObject> saveData(Integer spuId) {
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setId(spuId);
        List<GoodsDoc> goodsDocs = this.esGoodsInfo(spuDTO);
        elasticsearchRestTemplate.save( goodsDocs.get(0));

        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> delData(Integer spuId) {

        GoodsDoc goodsDoc = new GoodsDoc();
        goodsDoc.setId(spuId.longValue());

        elasticsearchRestTemplate.delete(goodsDoc);
        return this.setResultSuccess();
    }

    /**
     * 根据cid查询所有的规格参数
     * @param hotCid
     */
    private  Map<String, List<String>>  getSpecParam(Integer hotCid,String search){

        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(hotCid);
        specParamDTO.setSearching(true);//只搜索有查询属性的规格参数
        Result<List<SpecParamEntity>> specParamInfo = specificationFeign.getSpecParamInfo(specParamDTO);
        if (specParamInfo.getCode()==200){
            List<SpecParamEntity> specParamList = specParamInfo.getData();
            //  聚合查询
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(QueryBuilders.multiMatchQuery(search,"title","brandName","categoryName"));
            //es至少查询一条数据
            queryBuilder.withPageable(PageRequest.of(0,1));

            specParamList.stream().forEach(specParam ->{
                queryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));
            });

            //再次查询es库
            SearchHits<GoodsDoc> searchHits = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsDoc.class);
            Map<String, List<String>> map = new HashMap<>();
            Aggregations aggregations = searchHits.getAggregations();
            specParamList.stream().forEach(specParam ->{
                Terms terms = aggregations.get(specParam.getName());
                List<? extends Terms.Bucket> buckets = terms.getBuckets();
                List<String> valueList = buckets.stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());

                map.put(specParam.getName(),valueList);
            });

            return  map;
        }
        return null;
    }

    /**
     * 构建查询条件
     * @param search
     * @param page
     * @return
     */
    private  NativeSearchQueryBuilder getSearchQueryBuilder(String search, Integer page,String filter){
        //设置高级查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //System.out.println(filter);

        if (StringUtil.isNotEmpty(filter) && filter.length() >2){
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            Map<String, String> filterMap = JSONUtil.toMapValueString(filter);

            filterMap.forEach((key,value) ->{
                MatchQueryBuilder matchQueryBuilder  = null;
                //分类 品牌 规格参数 查询的方式不一样
                if(key.equals("cid3") || key.equals("brandId")){
                   matchQueryBuilder  = QueryBuilders.matchQuery(key, value);
                }else{
                   matchQueryBuilder  = QueryBuilders.matchQuery("specs." + key + ".keyword", value);
                }
                boolQueryBuilder.must(matchQueryBuilder);
            });
            queryBuilder.withFilter(boolQueryBuilder);
        }

        //查询多个字段
        queryBuilder.withQuery(QueryBuilders.multiMatchQuery(search,"title","brandName","categoryName"));
        //聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("cate_agg").field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms("brand_agg").field("brandId"));
        //设置高亮字段
        queryBuilder.withHighlightBuilder(EsHighLightUtil.getHighlightBuilder("title"));
        //分页
        queryBuilder.withPageable(PageRequest.of(page-1, 10));
        return  queryBuilder;
    }

    /**
     * 获取品牌集合
     * @param aggregations
     * @return
     */
    private List<BrandEntity> getBrandList(Aggregations aggregations){
        Terms brand_agg = aggregations.get("brand_agg");
        //聚合了所有品牌数据
        List<? extends Terms.Bucket> BrandBuckets = brand_agg.getBuckets();
        List<String> brandIdList = BrandBuckets.stream().map(brandbucket -> {
            Number keyAsNumber = brandbucket.getKeyAsNumber();
            Integer brandId = Integer.valueOf(keyAsNumber.intValue());
            return brandId + "";//得到品牌id,并且且转为String类型,方便接下来的操
        }).collect(Collectors.toList());
        //通过brandid获取brand详细数据
        //String.join(分隔符,List<String>),将list集合转为,分隔的字符串\
        Result<List<BrandEntity>> brandResult = brandFeign.getBrandByIds(String.join(",", brandIdList));
        return  brandResult.getData();
    }

    /**
     * 获取分类集合
     * @param aggregations
     * @return
     */
    private Map<Integer,List<CategoryEntity>> getCateList(Aggregations aggregations){
        Terms cate_agg = aggregations.get("cate_agg");
        //聚合了所有分类信息
        List<? extends Terms.Bucket> cateBuckets = cate_agg.getBuckets();

        Map<Integer, List<CategoryEntity>>  map = new HashMap<>();
        List<Integer> hotCidList = Arrays.asList(0);//获得分类中热度最高的
        List<Integer> maxCountList = Arrays.asList(0);

        List<String> cateList = cateBuckets.stream().map(cateucket -> {
            Number keyAsNumber = cateucket.getKeyAsNumber();
            Integer cateId = Integer.valueOf(keyAsNumber.intValue());
            if (maxCountList.get(0)<cateucket.getDocCount()){
                maxCountList.set(0,Long.valueOf(cateucket.getDocCount()).intValue());
                hotCidList.set(0,keyAsNumber.intValue());
            }
            return cateId + "";
        }).collect(Collectors.toList());

        //通过cateId获取category详细数据
        String cidsStr = String.join(",", cateList);
        Result<List<CategoryEntity>> caterogyResult = categoryFeign.getCategoryByIdList(cidsStr);
        //key为热度最高的cid value为cid集合对应的数据
        map.put(hotCidList.get(0),caterogyResult.getData());
        return map;
    }
}
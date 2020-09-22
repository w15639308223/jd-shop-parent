package com.mr.test;

import com.mr.RunTestEsApplication;
import com.mr.entity.GoodsEntity;
import com.mr.repository.GoodsEsRepository;
import com.mr.utils.ESHighLightUtil;
import org.assertj.core.internal.Bytes;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/14 20:43
 */

//在spring容器环境下执行
@RunWith(SpringRunner.class)
//生命启动类,当测试方法执行的时候会自动启动容器
@SpringBootTest(classes = {RunTestEsApplication.class})
public class EsTest {


    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private GoodsEsRepository goodsEsRepository;

    /**
     * 创建索引
     *
     */
    @Test
    public void createGoodsIndex(){
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of("indexname"));
        indexOperations.create();//创建索引
        //indexOperations.exists() 判断索引是否存在
        System.out.println(indexOperations.exists()?"索引创建成功":"索引创建失败");
    }

    /**
     * 创建映射
     */
    public void createGoodsMapping(){

        //此构造函数会检查有没有索引存在,如果没有则创建该索引,如果有则使用原来的索引
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsEntity.class);

        indexOperations.createMapping();//创建映射,不调用此函数也可以创建映射,这就是高版本的强大之处
        System.out.println("映射创建成功");
    }

    /**
     * 删除索引
     */
    @Test
    public void  deleteGoodsMapping(){
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsEntity.class);
        indexOperations.delete();
        System.out.println("索引删除成功");
    }

    /**
     * 新增文档
     */

    @Test
    public  void saveDate(){
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setId(1L);
        goodsEntity.setTitle("华为畅想3");
        goodsEntity.setBrand("华为");
        goodsEntity.setCategory("手机");
        goodsEntity.setPrice(3000D);

        goodsEsRepository.save(goodsEntity);

        System.out.println("新增成功");
    }

    @Test
    public  void  saveDataAll(){
        GoodsEntity entity = new GoodsEntity();
        entity.setId(2L);
        entity.setBrand("苹果");
        entity.setCategory("手机");
        entity.setImages("pingguo.jpg");
        entity.setPrice(5000D);
        entity.setTitle("iphone11手机");

        GoodsEntity entity2 = new GoodsEntity();
        entity2.setId(3L);
        entity2.setBrand("三星");
        entity2.setCategory("手机");
        entity2.setImages("sanxing.jpg");
        entity2.setPrice(3000D);
        entity2.setTitle("w2019手机");

        GoodsEntity entity3 = new GoodsEntity();
        entity3.setId(4L);
        entity3.setBrand("华为");
        entity3.setCategory("手机");
        entity3.setImages("huawei.jpg");
        entity3.setPrice(4000D);
        entity3.setTitle("华为mate30手机");

   /*    ArrayList<GoodsEntity> goodsEntities = new ArrayList<>();
        goodsEntities.add(entity);
        goodsEntities.add(entity2);
        goodsEntities.add(entity3);*/

        goodsEsRepository.saveAll(Arrays.asList(entity,entity2,entity3));

        System.out.println("批量新增成功");

    }
    /*
    * 更新文档
    * */
    @Test
    public void updateData(){

        GoodsEntity entity = new GoodsEntity();
        entity.setId(1L);
        entity.setBrand("小米");
        entity.setCategory("手机");
        entity.setImages("xiaomi.jpg");
        entity.setPrice(1000D);
        entity.setTitle("小米3");

        goodsEsRepository.save(entity);

        System.out.println("修改成功");
    }
    /*
    * 删除文档
    * */
    @Test
    public void delData(){

        GoodsEntity entity = new GoodsEntity();
        entity.setId(1L);

        goodsEsRepository.delete(entity);

        System.out.println("删除成功");
    }

    /**
     * 查询所有
     */
    @Test
    public void  searchAll(){
        //查询总条数
        long count = goodsEsRepository.count();
        System.out.println(count);
        //查询所有数据
        Iterable<GoodsEntity> all = goodsEsRepository.findAll();
        all.forEach(goodsEntity -> {
            System.out.println(goodsEntity);
        });
    }
    /**
     * 条件查询
     */
    @Test
    public  void  searchByParam(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("title","华为"))
               // .must(QueryBuilders.rangeQuery("price").gte(1000).lte(10000))
        );
        //分页 当前页数-1
        queryBuilder.withPageable(PageRequest.of(1-1,5));
        //排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));

        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);

        List<SearchHit<GoodsEntity>> searchHits = search.getSearchHits();
        searchHits.stream().forEach(hit ->  {
            GoodsEntity content = hit.getContent();
            System.out.println(content);
        });
    }

    @Test
    public  void highLightSearch(){
        //构建稿件查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //构建高亮查询
        HighlightBuilder builder = new HighlightBuilder();


        //设置高亮查询子段
        HighlightBuilder.Field title = new HighlightBuilder.Field("title");

        //设置高亮标签
        title.preTags("<font style='color:#e4393c'>");
        title.postTags("</font>");
        builder.field(title);
        //高亮查询
        queryBuilder.withHighlightBuilder(builder);

        queryBuilder.withQuery(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("title","华为手机"))
                       // .must(QueryBuilders.rangeQuery("price").gte(1000).lte(3000))

        );

        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);

        List<SearchHit<GoodsEntity>> searchHits = search.getSearchHits();

        //结果集
        List<GoodsEntity> title2 = searchHits.stream().map(hit -> {
            GoodsEntity goodsEntity = new GoodsEntity();
            //通过字段名获取高亮查询结果
            List<String> title1 = hit.getHighlightField("title");
            goodsEntity.setTitle(title1.get(0));

            return goodsEntity;
        }).collect(Collectors.toList());

    }

    @Test
    public  void highLightSearchTest(){
        //构建高级查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //高亮查询           高亮字段
        queryBuilder.withHighlightBuilder(ESHighLightUtil.getHighlightBuilder("title"));
        queryBuilder.withQuery(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("title","华为手机"))
                // .must(QueryBuilders.rangeQuery("price").gte(1000).lte(3000))

        );

        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);

        List<SearchHit<GoodsEntity>> searchHits = search.getSearchHits();

        List<SearchHit<GoodsEntity>> highLightHit = ESHighLightUtil.getHighLightHit(searchHits);

        System.out.println(highLightHit);

        List<GoodsEntity> collect = highLightHit.stream().map(searchHit -> {
            GoodsEntity content = searchHit.getContent();
            return content;
        }).collect(Collectors.toList());

        /*//结果集
        List<GoodsEntity> title2 = searchHits.stream().map(hit -> {
            GoodsEntity goodsEntity = new GoodsEntity();
            //通过字段名获取高亮查询结果
            List<String> title1 = hit.getHighlightField("title");
            goodsEntity.setTitle(title1.get(0));

            return goodsEntity;
        }).collect(Collectors.toList());*/

    }
    //聚合为桶
    @Test
    public void searchAgg(){
       /* //byte转换成string类型
      byte[] bytes = "".getBytes();
       //string转换byte
        try {
            String a = new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       */

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.addAggregation(
                AggregationBuilders.terms("brand_agg").field("brand")
        );

        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);

        Aggregations aggregations = search.getAggregations();

        //terms 是Aggregation的子类
        //Aggregation brand_agg = aggregations.get("brand_agg");/
        Terms terms = aggregations.get("brand_agg");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString() + ":" + bucket.getDocCount());
        });
        System.out.println(search);
    }

    /*
    聚合函数
     */
    @Test
    public void searchAggMethod(){

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.addAggregation(
                AggregationBuilders.terms("brand_agg")
                        .field("brand")
                        //聚合函数
                        .subAggregation(AggregationBuilders.max("max_price").field("price"))
        );

        SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);

        Aggregations aggregations = search.getAggregations();

        Terms terms = aggregations.get("brand_agg");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString() + ":" + bucket.getDocCount());

            //获取聚合
            Aggregations aggregations1 = bucket.getAggregations();
            //得到map
            Map<String, Aggregation> map = aggregations1.asMap();
            //需要强转,Aggregations是一个类 Terms是他的子类,Aggregation是一个接口Max是他的子接口,而且Max是好几个接口的子接口
            Max max_price = (Max) map.get("max_price");
            System.out.println(max_price.getValue());
        });
    }
    }


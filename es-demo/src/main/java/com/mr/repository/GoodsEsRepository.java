package com.mr.repository;

import com.mr.entity.GoodsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/14 21:11
 */
public interface GoodsEsRepository extends ElasticsearchRepository<GoodsEntity,Long> {

  /*  List<GoodsEntity> findAllByTitle(String title);

    List<GoodsEntity> findByAndPriceVetwen(Double start,Double end);*/
 }

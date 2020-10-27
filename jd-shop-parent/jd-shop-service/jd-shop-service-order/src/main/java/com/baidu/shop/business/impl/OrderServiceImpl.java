package com.baidu.shop.business.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.business.OrderService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.constant.MrShopConstant;
import com.baidu.shop.dto.CarDto;
import com.baidu.shop.dto.OrderDTO;
import com.baidu.shop.dto.OrderInfo;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.OrderDetailEntity;
import com.baidu.shop.entity.OrderEntity;
import com.baidu.shop.entity.OrderStatusEntity;
import com.baidu.shop.mapper.OrderDetailMapper;
import com.baidu.shop.mapper.OrderMapper;
import com.baidu.shop.mapper.OrderStatusMapper;
import com.baidu.shop.redis.repository.RedisRepository;
import com.baidu.shop.status.HttpStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.IdWorker;
import com.baidu.shop.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/21 15:45
 */

@RestController
public class OrderServiceImpl extends BaseApiService implements OrderService {

    @Autowired
    private OrderMapper orderMapper;//用户订单

    @Autowired
    private OrderDetailMapper orderDetailMapper;//订单详情

    @Autowired
    private OrderStatusMapper orderStatusMapper;//订单状态

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisRepository redisRepository;


    @Override
    public Result<String> createOrder(OrderDTO orderDTO, String token) {
        long orderId = idWorker.nextId();//通过雪花算法生成订单id
        //先获取用户信息
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            Date date = new Date();
            OrderEntity orderEntity = new OrderEntity();
            //orderEntity.setActualPay();
            //orderEntity.setTotalPay();
            orderEntity.setUserId(userInfo.getId()+"");//用户信息
            orderEntity.setOrderId(orderId);//订单id
            orderEntity.setBuyerNick(userInfo.getUsername());//用户名称
            orderEntity.setBuyerMessage("王泽辉很强");//留言
            orderEntity.setPaymentType(orderDTO.getPayType());
            orderEntity.setCreateTime(date);//创建订单时间
            orderEntity.setInvoiceType(1);//发票类型
            //orderEntity.setPaymentType(1);//支付类型
            orderEntity.setSourceType(1);//订单来源 写死的PC端,如果项目健全了以后,这个值应该是常量
            orderEntity.setBuyerRate(1);//买家是否评论

            //生成订单 detail
            List<Long> longs = Arrays.asList(0L);

            List<OrderDetailEntity> orderDetailList = Arrays.asList(orderDTO.getSkuIds().split(",")).stream().map(skuIdStr -> {

                //通过skuid查询redis -->sku数据
                CarDto carDto = redisRepository.getHash(MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId(), skuIdStr, CarDto.class);
                if (carDto ==null ){
                    throw  new RuntimeException("数据异常");
                }
                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                orderDetailEntity.setSkuId(Long.valueOf(skuIdStr));
                orderDetailEntity.setOrderId(orderId);
                orderDetailEntity.setImage(carDto.getImage());
                orderDetailEntity.setNum(carDto.getNum());
                orderDetailEntity.setOwnSpec(carDto.getOwnSpec());
                orderDetailEntity.setPrice(carDto.getPrice());
                orderDetailEntity.setTitle(carDto.getTitle());
                //totalPrice += car.getPrice() * car.getNum();
                longs.set(0,carDto.getNum() * carDto.getPrice()+ longs.get(0));

                return  orderDetailEntity;
            }).collect(Collectors.toList());

               orderEntity.setActualPay(longs.get(0));//实付金额
               orderEntity.setTotalPay(longs.get(0));//总金额


            //订单详情status
            OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
            orderStatusEntity.setCreateTime(date);
            orderStatusEntity.setOrderId(orderId);
            orderStatusEntity.setStatus(1);//已经创建订单,但是没有支付

            //入库
            orderMapper.insertSelective(orderEntity);
            orderDetailMapper.insertList(orderDetailList);
            orderStatusMapper.insertSelective(orderStatusEntity);

            //mysql和redis双写一致性问题?????
            //通过用户id和skuid删除购物车中的数据
            Arrays.asList(orderDTO.getSkuIds().split(",")).stream().forEach(skuidStr -> {
                redisRepository.delHash(MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId(),skuidStr);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("===============");
        return this.setResult(HttpStatus.OK,"",orderId + "");
    }

    @Override
    public Result<OrderInfo> getOrderInfoByOrderId(Long orderId) {
        //查询所有的订单id
        OrderEntity orderEntity = orderMapper.selectByPrimaryKey(orderId);
        //查询订单的详情
        OrderInfo orderInfo = BaiduBeanUtil.copyProperties(orderEntity, OrderInfo.class);

        Example example = new Example(OrderDetailEntity.class);
        example.createCriteria().andEqualTo("orderId",orderId);

        List<OrderDetailEntity> orderDetailEntities = orderDetailMapper.selectByExample(example);
        orderInfo.setOrderDetailList(orderDetailEntities);

        OrderStatusEntity orderStatusEntity = orderStatusMapper.selectByPrimaryKey(orderId);
        orderInfo.setOrderStatusEntity(orderStatusEntity);

        return this.setResultSuccess(orderInfo);

    }
}

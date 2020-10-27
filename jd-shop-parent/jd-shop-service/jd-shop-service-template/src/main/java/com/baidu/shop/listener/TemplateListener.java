package com.baidu.shop.listener;

import com.baidu.shop.constant.MqMessageConstant;
import com.baidu.shop.business.TemplateService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/12 19:15
 */
@Component
@Slf4j
public class TemplateListener {

    @Autowired
    private TemplateService templateService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = MqMessageConstant.SPU_QUEUE_PAGE_SAVE,
                            durable = "true"
                    ),
                    exchange = @Exchange(
                            value = MqMessageConstant.EXCHANGE,
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = {MqMessageConstant.SPU_ROUT_KEY_SAVE,MqMessageConstant.SPU_ROUT_KEY_UPDATE}
            )
    )
    public void save(Message message, Channel channel) throws IOException {

        log.info("template服务接受到需要保存数据的消息: " + new String(message.getBody()));
        //根据spuId生成页面
        templateService.createStaticHtmlTemplate(Integer.valueOf(new String(message.getBody())));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = MqMessageConstant.SPU_QUEUE_PAGE_DELETE,
                            durable = "true"
                    ),
                    exchange = @Exchange(
                            value = MqMessageConstant.EXCHANGE,
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = MqMessageConstant.SPU_ROUT_KEY_DELETE
            )
    )
    public void delete(Message message, Channel channel) throws IOException {

        log.info("template服务接受到需要删除数据的消息: " + new String(message.getBody()));
        //根据spuid删除页面
        templateService.delHTMLBySpuId(Integer.valueOf(new String(message.getBody())));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }
}

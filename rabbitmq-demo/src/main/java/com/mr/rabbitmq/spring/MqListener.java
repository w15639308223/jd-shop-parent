package com.mr.rabbitmq.spring;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/12 14:06
 */
@Component
public class MqListener {

        @RabbitListener(
                bindings = @QueueBinding(
                        value = @Queue(
                                value = "topic_exchange_queue_1",
                                durable = "true"
                        ),
                        exchange = @Exchange(
                                value = "topic_exchange_test",
                                ignoreDeclarationExceptions = "true",
                                type = ExchangeTypes.TOPIC
                        ),
                        key = {"#.#"}
                )
        )

        public static void main(String msg) {
            System.out.println("消费者接受到消息" + msg);
        }
}

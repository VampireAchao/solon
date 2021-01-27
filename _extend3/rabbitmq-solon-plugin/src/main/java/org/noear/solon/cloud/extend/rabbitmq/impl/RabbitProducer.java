package org.noear.solon.cloud.extend.rabbitmq.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.noear.solon.cloud.model.Event;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 *
 * @author noear
 * @since 1.3
 */
public class RabbitProducer {
    private RabbitConfig config;
    private Channel channel;
    private RabbitChannelFactory factory;

    public RabbitProducer(RabbitChannelFactory factory) {
        this.config = factory.getConfig();
        this.factory = factory;
    }

    /**
     * 绑定
     */
    public void bind() throws IOException, TimeoutException {
        channel = factory.getChannel();

        //
        // 与生产者有关
        //

        //1.声明交换机(String exchange:交换机名 , String type:交换机类型 , boolean durable:是否持久化 , boolean autoDelete:是否自动删除 , boolean internal:是否是内部交换机, Map arguments:交换机属性) throws IOException ;
        channel.exchangeDeclare(config.exchangeName,
                config.exchangeType,
                config.rabbit_durable,
                config.rabbit_autoDelete,
                config.rabbit_internal, new HashMap<>());
    }

    /**
     * 发布事件
     */
    public boolean publish(Event event) throws IOException {
        //String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body

        Map<String, Object> headers = new HashMap<>();
        headers.put("topic", event.topic());
        headers.put("tags", event.tags());

        AMQP.BasicProperties rabbit_props = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(headers)
                .build();

        if (event.scheduled() != null) {

        } else {

            //
            // 队列名，注意与消费者的关系
            //

            //basicPublish::String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body

            if (config.exchangeType == BuiltinExchangeType.FANOUT) {
                channel.basicPublish(
                        config.exchangeName,
                        "",
                        config.rabbit_mandatory,
                        rabbit_props,
                        event.content().getBytes(StandardCharsets.UTF_8));
            } else {
                channel.basicPublish(
                        config.exchangeName,
                        event.topic(),
                        config.rabbit_mandatory,
                        rabbit_props,
                        event.content().getBytes(StandardCharsets.UTF_8));
            }
        }

        return true;
    }
}

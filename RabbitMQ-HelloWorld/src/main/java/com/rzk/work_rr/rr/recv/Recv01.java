package com.rzk.work_rr.recv;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


/**
 * @PackageName : com.rzk.simple.recv
 * @FileName : Recv
 * @Description : 工作队列-轮询-消息接收
 * @Author : rzk
 * @CreateTime : 23/6/2021 上午12:22
 * @Version : 1.0.0
 */
public class Recv01 {
    private final static String QUEUE_NAME = "work_rr";

    public static void main(String[] argv) throws Exception {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("120.55.192.186");
        factory.setUsername("yeb");
        factory.setVirtualHost("/yeb");
        factory.setPassword("yeb");
        factory.setPort(5672);
        //连接工厂创建连接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //绑定队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            /**
             * 手动确认
             * multiple: 是否确认多条
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false );
        };
        /**
         * 监听队列消费消息
         * autoAck:自动应答
         * 当消费者收到该消息,会返回通知消息队列 我消费者已经收到消息了
         */
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}

package com.rzk.work_rr.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;


/**
 * @PackageName : com.rzk
 * @FileName : Send
 * @Description : 工作队列-轮询-消息生产者
 * @Author : rzk
 * @CreateTime : 23/6/2021 上午12:21
 * @Version : 1.0.0
 */
public class Send {

    //定义队列名称
    private final static String QUEUE_NAME = "work_rr";

    public static void main(String[] argv) throws Exception {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("*");
        factory.setUsername("yeb");
        factory.setVirtualHost("/yeb");
        factory.setPassword("yeb");
        factory.setPort(5672);

        try (
                //连接工厂创建连接
                Connection connection = factory.newConnection();
                //创建信道
                Channel channel = connection.createChannel()) {
            /**
             * 绑定队列
             * 声明队列
             *  第一个参数 queue ：队列名称
             *  第二个参数 durable ：是否持久化
             *  第三个参数 Exclusive ：排他队列，如果一个队列被声明为排他队列,该队列仅对首次声明的连接可见，并在连接断开时自动删除。
             * 这里需要注意三点：
             *      1 ．排他队列是基于连接可见的，同一连接的不同通道是可以同时访问同一个连接创建的排他他队
             *      2 . ”百次“，如果一个连接已经声明了一个排他队列，其他连接是不允许建立同名的；排他队列这个与昔通队歹怀同。
             *      3 ．即使该队列是持久化的，一旦连接关闭或者客户端退出，该排他队列都会被自动删除的。这种队列适用于只限于一个容户端发送读取消息的应用场景。
             * 第四个参数 Auto-delete ：自动删除，如果该队列没有任何订阅的消费者的话，该队列会被自动删除。这种队列适用于临时队列
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < 20; i++) {
                String message = " Send  "+i;
                //队列消息的生产者:发送消息
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'" + i);
            }

        }
    }
}





























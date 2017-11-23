package com.edu.sky.promotion.filter;

import com.alibaba.fastjson.JSON;
import com.edusky.message.api.DeviceType;
import com.edusky.message.api.message.MsgIdentity;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.PushMessageContent;
import com.edusky.message.api.toolkit.Objs;
import com.edusky.message.client.PushBuilder;
import com.edusky.message.client.PushClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component/*
@Slf4j*/
public class MyStartRunner /*implements CommandLineRunner*/{


    private static Logger logger = LoggerFactory.getLogger(MyStartRunner.class);

    @Value("${pushHost}")
    private String host;
    @Value("${pushPort}")
    private Integer port;

    private static PushClient client = new PushBuilder()
            .deviceType((byte)0)
            .openId("teaching")
            .getClient();

    public static PushClient getClient(){
        return client;
    }

    public static void main(String[] args) throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.connect("192.168.1.163",7007);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PushMessage message = PushMessage.buildRequestEntity();
                PushMessageContent content = message.getBody();
                int i = 1;
//                for (int i = 1; i < 4 ; i++) {
                    content.setContentBody("测试测试~~~~~测试测试");
                    content.setFrom(client.getIdentity());
                    content.setTo(MsgIdentity.builder().deviceType(Byte.parseByte(String.valueOf(i)))
                            .openId(i + "ffffffffffff").build());
                    client.sendMsg(message);
                    logger.info("PushClient 教师推送内容：" + JSON.toJSONString(content));
                    client.sendMsg(PushMessage.builder().build());
//                }
            }
        }).start();


    }


}

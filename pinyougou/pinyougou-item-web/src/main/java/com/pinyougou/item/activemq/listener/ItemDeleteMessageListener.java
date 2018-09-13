package com.pinyougou.item.activemq.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.File;

public class ItemDeleteMessageListener extends AbstractAdaptableMessageListener{
    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;
    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        //删除静态化页面
        ObjectMessage objectMessage = (ObjectMessage) message;
        Long[] goodsIds = (Long[]) objectMessage.getObject();
        for (Long goodsId : goodsIds) {
            String filename = ITEM_HTML_PATH + goodsId + ".html";
            File file = new File(filename);
            if (file.exists()){
                file.delete();
            }
        }
        System.out.println("删除同步完成");
    }
}

package com.ear.queueMsg;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * <p>Title:QueueSender</p>
 * <p>Description: Queue���з�ʽ���յ�Ե���Ϣ����</p>
 * <p>Company: ecar</p> 
 * @author  hexiaoyun
 * @date 2017-2-23 ����04:13:30
*/
public class QueueReceiver {
    // tcp ��ַ
    public static final String BROKER_URL = "tcp://localhost:61616";
    // Ŀ�꣬��ActiveMQ����Ա����̨���� http://localhost:8161/admin/queues.jsp
    public static final String TARGET = "hoo.mq.queue";
    
    public static void main(String[] args) throws Exception {
        QueueReceiver.run();
    }
    
    public static void run() throws Exception {
        QueueConnection connection = null;
        QueueSession session = null;
        try {
            // �������ӹ���
            QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
            // ͨ����������һ������
            connection = factory.createQueueConnection();
            // ��������
            connection.start();
            // ����һ��session�Ự
            session = connection.createQueueSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // ����һ����Ϣ����
            Queue queue = session.createQueue(TARGET);
            // ������Ϣ������
            javax.jms.QueueReceiver receiver = session.createReceiver(queue);
            receiver.setMessageListener(new MessageListener() { 
                public void onMessage(Message msg) { 
                    if (msg != null) {
                        MapMessage map = (MapMessage) msg;
                        try {
                            System.out.println(map.getLong("time") + "����#" + map.getString("text"));
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                } 
            }); 
            // ����10ms�ٹر�
            Thread.sleep(1000 * 10); 
            // �ύ�Ự
            session.commit();
            
        } catch (Exception e) {
            throw e;
        } finally {
            // �ر��ͷ���Դ
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}

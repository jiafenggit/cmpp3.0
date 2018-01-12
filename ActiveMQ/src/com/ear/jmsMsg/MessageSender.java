package com.ear.jmsMsg;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
	 * <p>Title:MessageSender</p>
	 * <p>Description: ʹ��JMS��ʽ������Ϣ</p>
	 * <p>Company: ecar</p> 
	 * @author hexiaoyun 
	 * @date 2017-2-23 ����03:46:45
 */
public class MessageSender {
	 // ���ʹ���
    public static final int SEND_NUM = 5;
    // tcp ��ַ
    public static final String BROKER_URL = "tcp://localhost:61616";
    // Ŀ�꣬��ActiveMQ����Ա����̨���� http://localhost:8161/admin/queues.jsp
    public static final String DESTINATION = "hoo.mq.message";
    
    public static void main(String[] args) throws Exception {
        MessageSender.run();
    }
    
    public static void run() throws Exception {
        Connection connection = null;
        Session session = null;
        try {
            // �������ӹ���
            ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
            // ͨ����������һ������
            connection = factory.createConnection();
            // ��������
            connection.start();
            // ����һ��session�Ự
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // ����һ����Ϣ����
            Destination destination = session.createQueue(DESTINATION);
            // ������Ϣ������
            MessageProducer producer = session.createProducer(destination);
            // ���ó־û�ģʽ
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //������Ϣ
            sendMessage(session, producer);
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

    /**
     * ������Ϣ
     * @param session
     * @param producer
     * @throws Exception
     */
    public static void sendMessage(Session session, MessageProducer producer) throws Exception {
        for (int i = 0; i < SEND_NUM; i++) {
            String message = "������Ϣ��" + (i + 1) + "��";
            TextMessage text = session.createTextMessage(message);
            System.out.println(message);
            producer.send(text);
        }
    }
    
}

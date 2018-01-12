package com.ear.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * JMSͨ�õĹ���
	    ��ȡ���ӹ���
		ʹ�����ӹ�����������
		��������
		�����Ӵ����Ự
		��ȡ Destination
		���� Producer����
		���� Producer
		���� message
		���� Consumer�����ͻ����message���ͻ���� message
		���� Consumer
		ע����Ϣ����������ѡ��
		���ͻ���� message
		�ر���Դ��connection, session, producer, consumer ��)
	 * <p>Title:JMSDemo</p>
	 * <p>Description: </p>
	 * <p>Company: </p> 
	 * @author ecar 
	 * @date 2017-2-21 ����06:06:36
 */
public class JMSDemo {

	ConnectionFactory connectionFactory;
    Connection connection;
    Session session;
    Destination destination;
    MessageProducer producer;
    MessageConsumer consumer;
    Message message;
    boolean useTransaction = false;
    
    public static void main(String[] args) {
    	JMSDemo demo = new JMSDemo();
    	try {
			demo.sendMessge();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private void sendMessge() throws Exception {
        try {
                Context ctx = new InitialContext();    //��ʼ��������
                connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactoryName");    //��ȡ���ӹ���
                //ʹ��ActiveMQʱ��connectionFactory = new ActiveMQConnectionFactory(user, password, getOptimizeBrokerUrl(broker));
                connection = connectionFactory.createConnection();   //ʹ�����ӹ�����������
                connection.start();   //��������
                session = connection.createSession(useTransaction, Session.AUTO_ACKNOWLEDGE);   //�����Ӵ����Ự
                destination = session.createQueue("TEST.QUEUE");    //����Destination
                //�����߷�����Ϣ
                producer = session.createProducer(destination);    //����Producer
                message = session.createTextMessage("this is a test");   //����Message

                //������ͬ������
                consumer = session.createConsumer(destination);     //����Consumer
                message = (TextMessage) consumer.receive(1000);
                System.out.println("Received message: " + message);
                //�������첽����
                consumer.setMessageListener(new MessageListener() {     //ע����Ϣ������
                        @Override
                        public void onMessage(Message message) {
                                if (message != null) {
                                        //doMessageEvent(message);      //������Ϣ�¼�
                                	try {
										producer.send(message);
									} catch (JMSException e) {
										e.printStackTrace();
									}
                                }
                        }
                });
        } catch (JMSException e) {
        	
        } finally {   //�ر���Դ
                producer.close();
                session.close();
                connection.close();
        }
    }
	
}


package MBeans;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


@Named(value = "messageBean")
@RequestScoped
public class MessageBean {
    
    @Resource(mappedName = "amqmsg")
    private Queue amqmsg;

    @Inject
    @JMSConnectionFactory("amqpool")
    private JMSContext context;
    
    private String msg;
    
    public void sendMSGtoQueue() {
        try {
            Connection connection = null;
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                    ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("amqmsg");
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            TextMessage message = session.createTextMessage(getMsg());
            producer.send(message);
            connection.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public MessageBean() {
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
}

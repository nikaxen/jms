
package mdb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "amqmsg")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MDBean implements MessageListener {
    
    @Resource(mappedName = "amqmsg")
    private Queue amqmsg;

    @Inject
    @JMSConnectionFactory("amqpool")
    private JMSContext context;
    
    
    
    public MDBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try { 
            System.out.println("MESSAGE: " + message.getJMSMessageID() + " DeliveryTime= " + message.getJMSDeliveryTime());
        } catch (JMSException ex) {
            Logger.getLogger(MDBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendMSGtoQueue(String msg) {
        context.createProducer().send(amqmsg, msg);
    }
    
}

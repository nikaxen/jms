
package mdb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;


@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "amqmsg")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MDBean implements MessageListener {
    
    @Resource(mappedName = "test")
    private Queue amqmsg;

    @Inject
    @JMSConnectionFactory("amqpool")
    private JMSContext context;
    
    
    
    public MDBean() {
    }
    
    @Override
    public void onMessage(Message message) {
    }
    
    private void sendMSGtoQueue(String msg) {
        context.createProducer().send(amqmsg, msg);
    }
    
}

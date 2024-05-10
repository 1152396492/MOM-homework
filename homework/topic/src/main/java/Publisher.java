import jakarta.jms.Connection;
import java.util.Random ; 
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Session;
import jakarta.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {

    private static String brokerURL = "tcp://localhost:61616";
    private static ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
	private Topic topic;
    
    public Publisher(String topicName) throws JMSException {
		
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	connection = factory.createConnection();
        
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = session.createTopic(topicName);
        producer = session.createProducer(topic);
		
		connection.start();
    }    
    
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }    
    
	public static void main(String[] args) throws JMSException {
    	Publisher publisher = new Publisher("MYTOPIC");
    	System.out.println(args[0]) ; 
    	while ( true ) {
    		publisher.sendMessage(args[0]);
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				publisher.close();
				e.printStackTrace();
			}
    	}
	}
	
    public void sendMessage(String UID ) throws JMSException {
        Random random = new Random( ) ;
    	int num = Integer.parseInt(UID) ;
        double mean = num ; 
        double stdDev = 1 ; 
        double randomNumner = random.nextGaussian() * stdDev + mean ; 
        Message message = session.createTextMessage(UID+"+"+Double.toString(randomNumner));
		//for(int i=0;i<15;i++){
		producer.send(message);
		System.out.println("Sent a message!");
		//}
    }	
    
    public void sendData ( String Data ) throws JMSException {
    	Message message = session.createTextMessage(Data) ; 
    	producer.send(message);
    	System.out.println ("Sent a Data!") ; 
    }

}

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import java.util.ArrayList ; 
import java.util.List; 

import org.apache.activemq.ActiveMQConnectionFactory;

public class ASyncConsumer {
	
	private List<String> messages ; 
	private List<Double>[] arrayOfLists = new List[5] ;  
	
	public ASyncConsumer ( ) {
		messages = new ArrayList<>() ; 
		for ( int i = 0 ; i < arrayOfLists.length ; i ++ ) {
			arrayOfLists[i] = new ArrayList<>() ; 
		}
	}
	
	public void storeMessage ( String message ) { 
		messages.add(message) ; 
		String[] parts = message.split("\\+") ; 
		String intPart = parts[0] ; 
		String doublePart = parts[1] ; 
		int uid = Integer.parseInt(intPart) ; 
		double val = Double.parseDouble(doublePart) ; 
		arrayOfLists[uid].add(val) ; 
	}
	
	public List<String> getMessages ( ) {
		return messages ; 
	}
	
	public void printMessages ( ) {
		System.out.println("Messages:") ; 
		for ( String message : messages ) {
			System.out.print ( "orz" + message ) ; 
		}
	}
	
    public static double calculateMean(List<Double> data) {
        double sum = 0;
        for (double value : data) {
            sum += value;
        }
        if ( data.size() == 0 ) return 0 ; 
        return sum / data.size();
    }

    public static double calculateVariance(List<Double> data, double mean) {
        double sumSquaredDiff = 0;
        for (double value : data) {
            sumSquaredDiff += Math.pow(value - mean, 2);
        }
        if ( data.size() == 0 ) return 0 ; 
        return sumSquaredDiff / data.size();
    }
	
	public void publisher ( String topicname ) throws JMSException {
		Publisher publisher = new Publisher("DATA");
		int n = 5 ; 
		for ( int i = 0 ; i < 5 ; i ++ ) {
			double mean = calculateMean (arrayOfLists[i].subList(Math.max(arrayOfLists[i].size()-n, 0),arrayOfLists[i].size())) ; 
			double variance = calculateVariance (arrayOfLists[i].subList(Math.max(arrayOfLists[i].size()-n, 0),arrayOfLists[i].size()),mean) ; 
			double Max = arrayOfLists[i].stream().mapToDouble(Double::doubleValue).max().orElse(0) ; 
			double Min = arrayOfLists[i].stream().mapToDouble(Double::doubleValue).min().orElse(0) ;
			//if ( Max == Double.NaN ) Max = 0 ; 
			//if ( Min == Double.NaN ) Min = 0 ; 
			double cur = 0 ; 
			if ( arrayOfLists[i].size() != 0 )
				cur = arrayOfLists[i].get(arrayOfLists[i].size()-1) ; 
			else 
				cur = 0 ; 
			String Data = Integer.toString(i)+"+"+Double.toString(mean)+"+"+Double.toString(variance)+"+"+Double.toString(Max)+"+"+Double.toString(Min)+"+"+Double.toString(cur); 
			publisher.sendData(Data);
		}
	}

    public static void main(String[] args) throws JMSException {
    	ASyncConsumer consumer = new ASyncConsumer ( ) ; 
    	
		String brokerURL = "tcp://localhost:61616";
		ConnectionFactory factory = null;
		Connection connection = null;
		Session session = null;
		Topic topic = null;
		MessageConsumer messageConsumer = null;
		MyListener listener = null;
		
        
		try {
			factory = new ActiveMQConnectionFactory(brokerURL);
			connection = factory.createConnection();
						
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = session.createTopic("MYTOPIC");

			messageConsumer = session.createConsumer(topic);
			
			listener = new MyListener(consumer);
			
			messageConsumer.setMessageListener(listener);
			
			connection.start();
			
			//consumer.printMessages();
			
			System.out.println("Press any key to exit.");
			System.in.read();   // Pause
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
}

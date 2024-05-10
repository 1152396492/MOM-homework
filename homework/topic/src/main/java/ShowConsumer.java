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

public class ShowConsumer {
	private List<Double>[][] arrayOfLists = new List[6][5] ; 
	RealTimeChart[] chart = new RealTimeChart[6] ; 
	// 数组1，2，3，4,5分别代表均值，方差，最大值，最小值,目前值
	public ShowConsumer ( ) {
		for ( int i = 1 ; i < 6 ; i ++ ) {
			for ( int j = 0 ; j < 5 ; j ++ ) {
				arrayOfLists[i][j] = new ArrayList<>() ; 
			}
		}
		for ( int i = 1 ; i < 6 ; i ++ ) {
			chart[i] = new RealTimeChart ( "History Data" , "UID"+Integer.toString(i) , 500 ) ; 
		}
	}

	
	public void paint ( ) {
		for ( int i = 1 ; i < 6 ; i ++ ) {
			if ( arrayOfLists[i][4].size() != 0 )
				chart[i].plot(arrayOfLists[i][4].get(arrayOfLists[i][4].size()-1));
		}
	}
	
	public void storeMessage ( String message ) { 
		String[] parts = message.split("\\+") ; 
		String uidPart = parts[0] ; 
		String meanPart = parts[1] ; 
		String variancePart = parts[2] ; 
		String maxPart = parts[3] ; 
		String minPart = parts[4] ; 
		String curPart = parts[5] ; 
		int uid = Integer.parseInt(uidPart) ; 
		double mean = Double.parseDouble(meanPart) ; 
		double variance = Double.parseDouble(variancePart) ;
		double Max = Double.parseDouble(maxPart) ; 
		double Min = Double.parseDouble(minPart) ; 
		double cur = Double.parseDouble(curPart) ; 
		arrayOfLists[uid][0].add(mean) ; 
		arrayOfLists[uid][1].add(variance) ;
		arrayOfLists[uid][2].add(Max) ;
		arrayOfLists[uid][3].add(Min) ;
		arrayOfLists[uid][4].add(cur) ; 
	}
	public static void main(String[] args) throws JMSException {
    	ShowConsumer consumer = new ShowConsumer ( ) ; 
    	
		String brokerURL = "tcp://localhost:61616";
		ConnectionFactory factory = null;
		Connection connection = null;
		Session session = null;
		Topic topic = null;
		MessageConsumer messageConsumer = null;
		MyListener2 listener = null;
		
        
		try {
			factory = new ActiveMQConnectionFactory(brokerURL);
			connection = factory.createConnection();
						
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = session.createTopic("DATA");

			messageConsumer = session.createConsumer(topic);
			
			listener = new MyListener2(consumer);
			
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

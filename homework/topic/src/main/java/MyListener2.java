import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

public class MyListener2 implements MessageListener {
	
	private ShowConsumer consumer ; 
	
	public MyListener2 ( ShowConsumer consumer ) {
		this.consumer = consumer ; 
	}

	public void onMessage(Message message) {
		try {
			String text = ((TextMessage) message).getText() ; 
			System.out.println("Received a message2: "+text);
			consumer.storeMessage(text);
			consumer.paint ( ) ; 
			//consumer.publisher("ACK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

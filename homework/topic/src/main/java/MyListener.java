import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

public class MyListener implements MessageListener {
	
	private ASyncConsumer consumer ; 
	
	public MyListener ( ASyncConsumer consumer ) {
		this.consumer = consumer ; 
	}

	public void onMessage(Message message) {
		try {
			String text = ((TextMessage) message).getText() ; 
			System.out.println("Received a message: "+text);
			consumer.storeMessage(text);
			consumer.publisher("ACK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package messageSistem;

import messageSistem.msg.Msg;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by artur on 23.05.14.
 */
public class MessageSystem {
    private Map<Address, ConcurrentLinkedQueue<Msg>> messages = new HashMap<Address, ConcurrentLinkedQueue<Msg>>();
    private AddressService addressService = new AddressService();

    public void addService(Subscriber subscriber) {
        messages.put(subscriber.getAddress(), new ConcurrentLinkedQueue<Msg>());
    }

    public void sendMessage(Msg message) {
        Queue<Msg> messageQueue = messages.get(message.getTo());
        messageQueue.add(message);
    }

    public void execForSubscriber(Subscriber subscriber) {
        Queue<Msg> messageQueue = messages.get(subscriber.getAddress());
        if(messageQueue == null){
            return;
        }
        while(!messageQueue.isEmpty()) {
            Msg message = messageQueue.poll();
            message.exec(subscriber);
        }
    }

    public AddressService getAddressService() {
        return addressService;
    }
}

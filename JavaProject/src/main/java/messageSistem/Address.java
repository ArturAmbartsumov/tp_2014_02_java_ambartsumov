package messageSistem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by artur on 23.05.14.
 */
public class Address {
    static private AtomicInteger subscriberIdCreator = new AtomicInteger();
    final private int subscriberId;

    public Address(){
        this.subscriberId = subscriberIdCreator.incrementAndGet();
    }

    public int hashCode(){
        return subscriberId;
    }
}

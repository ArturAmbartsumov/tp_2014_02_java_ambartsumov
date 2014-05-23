package messageSistem.msg;

import frontend.Frontend;
import messageSistem.Address;
import messageSistem.Subscriber;

/**
 * Created by artur on 23.05.14.
 */
public abstract class Msg_toFE extends Msg {

    public Msg_toFE(Address from, Address to) {
        super(from, to);
    }

    public void exec(Subscriber subscriber) {
        if(subscriber instanceof Frontend){
            exec((Frontend)subscriber);
        }
    }

    abstract void exec(Frontend frontend);
}

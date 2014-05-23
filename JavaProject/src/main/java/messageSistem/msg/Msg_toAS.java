package messageSistem.msg;

import dbService.AccountService;
import messageSistem.Address;
import messageSistem.Subscriber;

/**
 * Created by artur on 23.05.14.
 */
public abstract class Msg_toAS extends Msg {

    public Msg_toAS(Address from, Address to) {
        super(from, to);
    }

    public void exec(Subscriber subscriber) {
        if(subscriber instanceof AccountService){
            exec((AccountService)subscriber);
        }
    }

    abstract void exec(AccountService frontend);
}

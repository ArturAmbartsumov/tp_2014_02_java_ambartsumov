package messageSistem;

/**
 * Created by artur on 23.05.14.
 */
public class AddressService {
    private Address accountService;
    private Address frontend;

    public Address getAccountService() {
        return accountService;
    }

    public void setAccountService(Address accountService) {
        this.accountService = accountService;
    }

    public Address getFrontend() {
        return frontend;
    }

    public void setFrontend(Address frontend) {
        this.frontend = frontend;
    }
}

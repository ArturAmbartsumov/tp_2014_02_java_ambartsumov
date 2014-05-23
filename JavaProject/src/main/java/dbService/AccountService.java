package dbService;


import dbService.DAO.UserDAOImpl;
import exceptions.AccountServiceException;
import exceptions.WrongDataException;
import exceptions.EmptyDataException;
import dbService.models.UserDataSet;
import messageSistem.Address;
import messageSistem.MessageSystem;
import messageSistem.Sleeper;
import messageSistem.Subscriber;

import java.sql.SQLException;

/**
 * Created by artur on 29.03.14.
 */
public class AccountService implements Subscriber, Runnable {

    UserDAOImpl userDAO;
    private MessageSystem messageSystem;
    private Address address;

    public AccountService(MessageSystem ms) {
        this.messageSystem = ms;
        this.userDAO = new UserDAOImpl();

        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setAccountService(address);
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            messageSystem.execForSubscriber(this);
            Sleeper.sleep(Sleeper.TICK);
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public UserDataSet authorization(String login, String password) throws AccountServiceException,
                                                                                EmptyDataException,
                                                                                WrongDataException {

        if (login.equals("") || login == null || password.equals("") || password == null) {
            System.out.println("\nОшибка: заполните форму\n");
            throw new EmptyDataException("Форма не заполнена");
        }
        UserDataSet user;
        try {
            user = userDAO.getUserByNameAndPass(login, password);
            if (user == null) {
                System.out.println("\nОшибка: в базе не найден такой пользователь((\n");
                throw new WrongDataException("Ошибка: в базе не найден такой пользователь((");
            }
        }
        catch (SQLException e) {
            System.out.println("\nОшибка: выброс SQLException при авторизации: " + e.getMessage() + "\n");
            e.printStackTrace();
            throw new AccountServiceException("Ошибка: выброс SQLException при авторизации!");
        }
        return user;
    }

    public void registration(String login, String email, String password) throws AccountServiceException,
                                                                                      EmptyDataException,
                                                                                      WrongDataException {
        if(login == null || email == null || password == null ||
        login.equals("") || email.equals("") || password.equals("")) {
            System.out.println("\nОшибка: заполните форму\n");
            throw new EmptyDataException("Заполните форму!");
        }

        try {
            UserDataSet checkUser = userDAO.getUserByName(login);
            if (checkUser != null)
                throw new AccountServiceException("Ошибка: найден пользователь с таким же именем!");
            UserDataSet checkEmail = userDAO.getUserByEmail(email);
            if (checkEmail != null)
                throw new AccountServiceException("Ошибка: найден пользователь с таким же маилом!");
            UserDataSet user = new UserDataSet(login, email, password);
            userDAO.addUser(user);
        } catch (SQLException e) {
            System.out.println("\nОшибка: выброс SQLException при регистрации: " + e.getMessage() + "\n");
            e.printStackTrace();
            throw new AccountServiceException("Ошибка: выброс SQLException при регистрации!");
        }
    }
}

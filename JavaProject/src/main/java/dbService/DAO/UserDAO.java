package dbService.DAO;

import dbService.models.UserDataSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by artur on 11.03.14.
 */
public interface UserDAO {
    public void addUser(UserDataSet user) throws SQLException;
    public void updateUser(UserDataSet bus) throws SQLException;
    public void updateUserByName(UserDataSet bus) throws SQLException;
    public UserDataSet getUserById(Integer user_id) throws SQLException;
    public UserDataSet getUserByName(String name) throws SQLException;
    public UserDataSet getUserByEmail(String email) throws SQLException;
    public UserDataSet getUserByNameAndPass(String name, String password) throws SQLException;
    public List<UserDataSet> getAllUsers() throws SQLException;
    public void deleteUser(UserDataSet user) throws SQLException;
}

package dbService.DAO;

import dbService.DatabaseService;
import dbService.models.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by artur on 11.03.14.
 */
public class UserDAOImpl implements UserDAO {
    @Override
    public void addUser(UserDataSet user) throws SQLException {
        Session session = null;
        try {
            session = DatabaseService.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в функции addUser" + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {

                session.close();
            }
        }
    }

    @Override
    public void updateUser(UserDataSet user) throws SQLException {
        Session session = null;
        try {
            session = DatabaseService.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в функции updateUser" + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void updateUserByName(UserDataSet user) throws SQLException {
        Session session = null;
        UserDataSet currentUser = null;
        try {
            session = DatabaseService.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(UserDataSet.class);
            criteria.add(Restrictions.eq("username", user.getUsername()));
            currentUser = (UserDataSet) criteria.uniqueResult();
        } catch (Exception e) {
            System.out.println("Ошибка в функции updateUserByName" + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        user.setId(currentUser.getId());
        updateUser(user);
    }

    @Override
    public UserDataSet getUserById(Integer user_id) throws SQLException {
        Session session = null;
        UserDataSet user = null;
        try {
            session = DatabaseService.getSessionFactory().openSession();
            user = (UserDataSet) session.load(UserDataSet.class, user_id);
        } catch (Exception e) {
            System.out.println("Ошибка в функции getUserById" + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public UserDataSet getUserByName(String name) throws SQLException {
        Session session = null;
        UserDataSet user = null;
        try {
            session = DatabaseService.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(UserDataSet.class);
            criteria.add(Restrictions.eq("username", name));
            user = (UserDataSet) criteria.uniqueResult();
        } catch (Exception e) {
            System.out.println("Ошибка в функции getUserByName" + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public UserDataSet getUserByEmail(String email) throws SQLException {
        Session session = null;
        UserDataSet user = null;
        try {
            session = DatabaseService.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(UserDataSet.class);
            criteria.add(Restrictions.eq("email", email));
            user = (UserDataSet) criteria.uniqueResult();
        } catch (Exception e) {
            System.out.println("Ошибка в функции getUserByEmail" + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public UserDataSet getUserByNameAndPass(String name, String password) throws SQLException {
        Session session = null;
        UserDataSet user = null;
        try {
            session = DatabaseService.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(UserDataSet.class);
            criteria.add(Restrictions.eq("username", name))
                    .add(Restrictions.eq("password", password));
            user = (UserDataSet) criteria.uniqueResult();
        } catch (Exception e) {
            System.out.println("Ошибка в функции getUserByNameAndPass " + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    @Override
    public List<UserDataSet> getAllUsers() throws SQLException {
        Session session = null;
        List users = new ArrayList<UserDataSet>();
        try {
            session = DatabaseService.getSessionFactory().openSession();
            users = (ArrayList<UserDataSet>) session.createCriteria(UserDataSet.class).list();
        } catch (Exception e) {
            System.out.println("Ошибка в функции getAllUsers" + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return users;
    }

    @Override
    public void deleteUser(UserDataSet user) throws SQLException {
        Session session = null;
        try {
            session = DatabaseService.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в функции deleteUser" + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}

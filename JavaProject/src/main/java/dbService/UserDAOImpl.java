package dbService;

import models.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.swing.*;
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
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при добавлении пользователя" + e.getMessage(), "Ошибка при вставке", JOptionPane.WARNING_MESSAGE);
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
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при обновлении пользователя" + e.getMessage(), "Ошибка при вставке", JOptionPane.WARNING_MESSAGE);
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
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(UserDataSet.class);
            criteria.add(Restrictions.eq("username", user.getUsername()));
            currentUser = (UserDataSet) criteria.uniqueResult();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при обновлении пользователя" + e.getMessage(), "Ошибка при вставке", JOptionPane.WARNING_MESSAGE);
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
            session = HibernateUtil.getSessionFactory().openSession();
            user = (UserDataSet) session.load(UserDataSet.class, user_id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка поиска пользователя по id" + e.getMessage(), "Ошибка 'findById'", JOptionPane.WARNING_MESSAGE);
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
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(UserDataSet.class);
            criteria.add(Restrictions.eq("username", name));
            user = (UserDataSet) criteria.uniqueResult();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка поиска пользователя по имени" + e.getMessage(), "Ошибка 'findByNameAndPassword'", JOptionPane.WARNING_MESSAGE);
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    public UserDataSet getUserByEmail(String email) throws SQLException {
        Session session = null;
        UserDataSet user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(UserDataSet.class);
            criteria.add(Restrictions.eq("email", email));
            user = (UserDataSet) criteria.uniqueResult();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка поиска пользователя по имени" + e.getMessage(), "Ошибка 'findByNameAndPassword'", JOptionPane.WARNING_MESSAGE);
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    public UserDataSet getUserByNameAndPass(String name, String password) throws SQLException {
        Session session = null;
        UserDataSet user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(UserDataSet.class);
            criteria.add(Restrictions.eq("username", name))
                    .add(Restrictions.eq("password", password));
            user = (UserDataSet) criteria.uniqueResult();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка поиска пользователя по имени" + e.getMessage(), "Ошибка 'findByNameAndPassword'", JOptionPane.WARNING_MESSAGE);
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
            session = HibernateUtil.getSessionFactory().openSession();
            users = (ArrayList<UserDataSet>) session.createCriteria(UserDataSet.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка получения списка всех пользователей" + e.getMessage(), "Ошибка 'getAll'", JOptionPane.WARNING_MESSAGE);
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
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при удалинии пользователя" + e.getMessage(), "Ошибка при удалении", JOptionPane.WARNING_MESSAGE);
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}

package dbService;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by artur on 11.03.14.
 */

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration conf = new Configuration();
            conf.configure();
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
            builder.applySettings(conf.getProperties());
            ServiceRegistry serviceRegistry = builder.build();
            sessionFactory = conf.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.out.println("/nВыброс исключения в HibernateUtil/n");
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

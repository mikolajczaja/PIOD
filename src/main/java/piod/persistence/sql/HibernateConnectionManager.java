package piod.persistence.sql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConnectionManager {

    private static Log LOGGER = LogFactory.getLog(HibernateConnectionManager.class);
    private static HibernateConnectionManager instance;
    private static SessionFactory sessionFactory;

    private HibernateConnectionManager() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            LOGGER.error("Failed to create Session Factory object, cause: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static HibernateConnectionManager getInstance() {
        if (instance == null) {
            instance = new HibernateConnectionManager();
        }
        return instance;
    }

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
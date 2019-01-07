package piod.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import piod.model.Car;
import piod.model.Driver;
import piod.model.JsonParseable;
import piod.persistence.PersistenceConstants;
import piod.persistence.sql.HibernateConnectionManager;

import java.util.LinkedList;
import java.util.List;

public class PersistenceManager {

    private static Log LOGGER = LogFactory.getLog(PersistenceManager.class);

    @SuppressWarnings("unchecked")
    public List<String> fetchAllTableNamesFromDatabase() {
        Transaction tx = null;
        List<String> tableNamesList = new LinkedList<>();
        Session session = HibernateConnectionManager.getInstance().getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();
            String fetchAllTableNamesFromDatabaseQuery =
                    "SELECT table_name FROM information_schema.tables where table_schema='" + PersistenceConstants.DATABASE_NAME + "';";
            tableNamesList = session.createSQLQuery(fetchAllTableNamesFromDatabaseQuery).list();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error(e);
        } finally {
            session.close();
        }
        return tableNamesList;
    }

    public void createDummyTables() {
        Transaction tx = null;
        Session session = HibernateConnectionManager.getInstance().getSessionFactory().openSession();

        try {
            tx = session.beginTransaction();

            Car car1 = new Car("black");
            session.save(car1);

            Driver driver1 = new Driver("Stefan Kowalski", 21, car1);
            car1.getCrew().add(driver1);
            session.save(driver1);

            Driver driver2 = new Driver("Krzysztof Holowczyc", 38, car1);
            car1.getCrew().add(driver2);
            session.save(driver2);


            Car car2 = new Car("white");
            session.save(car2);

            Driver driver3 = new Driver("Kamil Nowak", 19, car2);
            car2.getCrew().add(driver3);
            session.save(driver3);

            Driver driver4 = new Driver("Adam Kowalczyk", 31, car2);
            car2.getCrew().add(driver4);
            session.save(driver4);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error(e);
        } finally {
            session.close();
        }
    }

    public List<JsonParseable> fetchAllJsonParseablesFromTables(List<String> tableNames) {
        Session session = HibernateConnectionManager.getInstance().getSessionFactory().openSession();
        Transaction tx = null;
        List<JsonParseable> allJsonParseables = new LinkedList<>();

        try {
            tx = session.beginTransaction();

            for (String singleTableName : tableNames) {
                List resultSet = session.createQuery("FROM " + StringUtils.capitalize(singleTableName)).list();
                for (Object singleResult : resultSet) {
                    if (singleResult instanceof JsonParseable) {
                        allJsonParseables.add((JsonParseable) singleResult);
                    }
                }
            }
            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error(e);
        } finally {
            session.close();
        }
        return allJsonParseables;
    }

}

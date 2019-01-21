package piod.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jooq.*;
import org.jooq.impl.DSL;
import piod.persistence.PersistenceConstants;
import piod.persistence.sql.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PersistenceManager {

    private static final String SELECT_ALL_TABLE_NAMES_QUERY = "SELECT table_name FROM information_schema.tables where table_schema='";
    private static Log LOGGER = LogFactory.getLog(PersistenceManager.class);

    public List<Result<Record>> fetchAllContentsFromTables(List<String> tableNames, ConnectionManager connectionManager) {
        List<Result<Record>> results = new LinkedList<>();

        try (Connection conn = connectionManager.getConnection()) {
            DSLContext create = DSL.using(conn, SQLDialect.MARIADB);

            for (String singleTableName : tableNames) {
                results.add(create.select().from(singleTableName).fetch());
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error(e);
        }
        return results;
    }

    public List<String> fetchAllTableNamesFromDatabase(ConnectionManager connectionManager) {
        List<String> results = new LinkedList<>();

        try (Connection conn = connectionManager.getConnection()) {
            DSLContext create = DSL.using(conn, SQLDialect.MARIADB);

            Result<Record> resultSet = create.fetch(SELECT_ALL_TABLE_NAMES_QUERY + PersistenceConstants.DATABASE_NAME + "';");
            for (Record record : resultSet) {
                Field<?>[] fields = record.fields();
                if (fields.length == 1) {
                    Object fieldValue = record.get(fields[0]);
                    if (fieldValue instanceof String) {
                        results.add((String) fieldValue);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error(e);
        }
        return results;
    }
}
package persistence.sql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import persistence.DatabaseConnectionInfo;
import persistence.PersistenceConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Log LOGGER = LogFactory.getLog(ConnectionManager.class);

    private Connection connection;
    private Boolean isConnected = false;

    public ConnectionManager(DatabaseConnectionInfo databaseConnectionInfo) {
        connect(databaseConnectionInfo);
    }

    public Connection getConnection() {
        return connection;
    }

    public Boolean getConnected() {
        return isConnected;
    }
    public static final String DRIVER_INFO = "com.mysql.cj.jdbc.Driver";

    private void connect(DatabaseConnectionInfo databaseConnectionInfo) {
        try {

            try {
                Class.forName(DRIVER_INFO);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection(getDatabaseInfo(databaseConnectionInfo.getDatabaseName()),
                    databaseConnectionInfo.getUserName(), databaseConnectionInfo.getPassword());
            isConnected = true;
        } catch (SQLException e) {
            LOGGER.error("database connection error, reason: " + e);
        }
    }

    private String getDatabaseInfo(String databaseName) {
        return PersistenceConstants.DATABASE_DRIVER
                + PersistenceConstants.DATABASE_URL
                + databaseName
                + PersistenceConstants.DATABASE_TIMEZONE;
    }
}


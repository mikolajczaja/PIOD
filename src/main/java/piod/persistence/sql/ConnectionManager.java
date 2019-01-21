package piod.persistence.sql;

import piod.persistence.DatabaseConnectionInfo;
import piod.persistence.PersistenceConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private Connection connection;
    private Boolean connected = false;
    private DatabaseConnectionInfo databaseConnectionInfo;

    public ConnectionManager(DatabaseConnectionInfo databaseConnectionInfo) throws SQLException, ClassNotFoundException {
        this.databaseConnectionInfo = databaseConnectionInfo;
        connect();
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection.isClosed()) {
            connect();
        }
        return connection;
    }

    public Boolean isConnected() {
        return connected;
    }

    public String getDatabaseName() {
        return databaseConnectionInfo.getDatabaseName();
    }

    private void connect() throws SQLException, ClassNotFoundException {

        Class.forName(DRIVER_CLASS);
        connection = DriverManager.getConnection(getDatabaseInfo(databaseConnectionInfo.getDatabaseName()),
                databaseConnectionInfo.getUserName(), databaseConnectionInfo.getPassword());
        connected = true;
    }

    private String getDatabaseInfo(String databaseName) {
        return PersistenceConstants.DATABASE_DRIVER
                + PersistenceConstants.DATABASE_URL
                + databaseName
                + PersistenceConstants.DATABASE_TIMEZONE;
    }
}


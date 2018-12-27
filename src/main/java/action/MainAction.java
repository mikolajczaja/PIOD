package action;

import persistence.DatabaseConnectionInfo;
import persistence.sql.ConnectionManager;

import java.sql.Connection;
import java.util.Scanner;

public class MainAction implements Action {

    @Override
    public void execute() {

        DatabaseConnectionInfo databaseConnectionInfo = readDatabaseConnectionInfoFromUser();

        ConnectionManager connectionManager = new ConnectionManager(databaseConnectionInfo);
        Connection connection = connectionManager.getConnection();
        //TODO pass connection to next action
        //TODO create action picker?

    }

    private DatabaseConnectionInfo readDatabaseConnectionInfoFromUser() {

        Scanner sc = new Scanner(System.in);
        DatabaseConnectionInfo databaseConnectionInfo = new DatabaseConnectionInfo();

        System.out.println("please enter database name");
        databaseConnectionInfo.setDatabaseName(sc.next());

        System.out.println("please enter username");
        databaseConnectionInfo.setUserName(sc.next());

        System.out.println("please enter password");
        databaseConnectionInfo.setPassword(sc.next());

        return databaseConnectionInfo;
    }
}

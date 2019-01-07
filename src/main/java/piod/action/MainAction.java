package piod.action;

import piod.model.JsonParseable;
import piod.persistence.DatabaseConnectionInfo;
import piod.persistence.sql.HibernateConnectionManager;

import java.util.List;
import java.util.Scanner;

public class MainAction implements Action {

    @Override
    public void execute() {

        HibernateConnectionManager connectionManager = HibernateConnectionManager.getInstance();
        connectionManager.createDummyTables();
        List<String> tableNames = connectionManager.fetchAllTableNamesFromDatabase();
        System.out.println("tableNames = " + tableNames);
        List<JsonParseable> jsonParseables = connectionManager.fetchAllJsonParseablesFromTables(tableNames);
        System.out.println("jsonParseables = " + jsonParseables);

        //TODO pass connection to next piod.action
        //TODO create piod.action picker?
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

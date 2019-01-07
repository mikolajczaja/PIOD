package piod.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import piod.model.JsonParseable;
import piod.persistence.DatabaseConnectionInfo;
import piod.persistence.sql.HibernateConnectionManager;
import piod.service.PersistenceManager;

import java.util.List;
import java.util.Scanner;

public class MainAction implements Action {
    private static Log LOGGER = LogFactory.getLog(MainAction.class);

    @Override
    public void execute() {

        PersistenceManager persistenceManager = new PersistenceManager();
        persistenceManager.createDummyTables();
        List<String> tableNames = persistenceManager.fetchAllTableNamesFromDatabase();
        System.out.println("tableNames = " + tableNames);
        List<JsonParseable> jsonParseables = persistenceManager.fetchAllJsonParseablesFromTables(tableNames);
        for(JsonParseable singleJsonParseable:jsonParseables){
            try {
                System.out.println(singleJsonParseable.toJson());
            } catch (JsonProcessingException e) {
                LOGGER.error(e);
            }
        }

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

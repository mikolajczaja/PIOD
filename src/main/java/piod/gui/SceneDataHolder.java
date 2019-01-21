package piod.gui;

import javafx.stage.Stage;
import piod.persistence.sql.ConnectionManager;

import java.util.List;

public class SceneDataHolder {

    private static SceneDataHolder instance;
    private List<String> tableNames;
    private Stage stage;
    private ConnectionManager connectionManager;

    public static SceneDataHolder getInstance() {
        if (instance == null) {
            instance = new SceneDataHolder();
        }
        return instance;
    }

    public static SceneDataHolder createInstance(Stage stage) {
        getInstance();
        instance.stage = stage;
        return instance;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Stage getStage() {
        return stage;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }
}
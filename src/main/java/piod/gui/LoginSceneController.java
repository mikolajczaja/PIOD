package piod.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.tools.StringUtils;
import piod.persistence.DatabaseConnectionInfo;
import piod.persistence.sql.ConnectionManager;
import piod.service.PersistenceManager;

import java.io.IOException;
import java.sql.SQLException;

public class LoginSceneController {

    @FXML
    private TextField databaseNameTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;


    public void changeToMainScene() throws IOException {

        PersistenceManager persistenceManager = new PersistenceManager();
        ConnectionManager connectionManager;
        try {
            connectionManager = new ConnectionManager(readDatabaseConnectionInfoFromUser());
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            return;
        }

        SceneDataHolder.getInstance().setConnectionManager(connectionManager);
        SceneDataHolder.getInstance().setTableNames(persistenceManager.fetchAllTableNamesFromDatabase(connectionManager));

        Stage stage = SceneDataHolder.getInstance().getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/mainScene.fxml"));
        stage.setTitle("Main Window");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private DatabaseConnectionInfo readDatabaseConnectionInfoFromUser() throws SQLException {

        DatabaseConnectionInfo databaseConnectionInfo = new DatabaseConnectionInfo();
        databaseConnectionInfo.setDatabaseName(databaseNameTextField.getText());
        databaseConnectionInfo.setUserName(userNameTextField.getText());
        databaseConnectionInfo.setPassword(passwordField.getText());

        if (StringUtils.isEmpty(databaseConnectionInfo.getDatabaseName())) {
            throw new SQLException("Database name is empty.");
        }
        return databaseConnectionInfo;
    }
}

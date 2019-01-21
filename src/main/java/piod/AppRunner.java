package piod;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import piod.gui.SceneDataHolder;

import java.io.IOException;

public class AppRunner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Stage stage = SceneDataHolder.createInstance(primaryStage).getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/loginScene.fxml"));
        stage.setTitle("Login Window");
        stage.setScene(new Scene(root));
        stage.show();
    }
}

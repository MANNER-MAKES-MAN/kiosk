package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/TypeSelect.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("매장 / 포장 선택");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

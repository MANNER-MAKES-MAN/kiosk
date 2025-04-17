package Figma_change;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MyApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // FXML 파일 로드
            Parent root = FXMLLoader.load(getClass().getResource("payment.fxml"));
            // Parent root = FXMLLoader.load(getClass().getResource("option.fxml"));

            // Scene 설정
            Scene scene = new Scene(root);

            // Stage 설정
            primaryStage.setTitle("Coffee");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

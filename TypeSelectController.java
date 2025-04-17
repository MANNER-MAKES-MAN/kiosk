package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class TypeSelectController {

    @FXML private Button storeButton;
@FXML private Button takeoutButton;

@FXML
public void initialize() {
    storeButton.setOnAction(e -> switchToMenu());
    takeoutButton.setOnAction(e -> switchToMenu());
}

private void switchToMenu() {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/MenuView.fxml"));
        Stage stage = (Stage) storeButton.getScene().getWindow(); // 또는 takeoutButton 사용
        stage.setScene(new Scene(root));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
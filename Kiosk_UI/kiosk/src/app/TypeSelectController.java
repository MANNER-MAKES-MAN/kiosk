package app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TypeSelectController {

    @FXML private Button storeButton;
    @FXML private Button takeoutButton;
    @FXML private Button voiceTestButton;

    @FXML
    public void initialize() {
        storeButton.setOnAction(e -> switchToMenu());
        takeoutButton.setOnAction(e -> switchToMenu());

        voiceTestButton.setOnAction(e -> handleVoiceTest());
    }

    private void switchToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MenuView.fxml"));
            Stage stage = (Stage) storeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleVoiceTest() {
        try {
            // 파이썬 스크립트 경로 설정
            ProcessBuilder pb = new ProcessBuilder("python", "C:\\Voice_test\\voice_test.py");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 결과 표시
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("음성 인식 결과");
            alert.setHeaderText("음성 인식 결과");
            alert.setContentText(output.toString());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("오류");
            errorAlert.setHeaderText("음성 인식 실패");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }
}
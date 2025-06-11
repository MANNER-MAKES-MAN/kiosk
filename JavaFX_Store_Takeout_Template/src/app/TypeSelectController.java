package app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeSelectController {

    @FXML
    private Button storeButton;
    @FXML
    private Button takeoutButton;
    @FXML
    private Button voiceTestButton;
    @FXML
    private TextField uidField;

    private final Map<String, CartItem> uidToItemMap = new HashMap<>();
    private final Map<String, CartItem> nameToItemMap = new HashMap<>();

    @FXML
    public void initialize() {

        loadItemsFromFile("coffees.txt");
        storeButton.setOnAction(e -> switchToMenu());
        takeoutButton.setOnAction(e -> switchToMenu());

        voiceTestButton.setOnAction(e -> handleVoiceTest());

    }

    private void loadItemsFromFile(String filename) {

        // ✅ UID 입력 처리
        uidField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String uid = uidField.getText().trim();
                if (uidToItemMap.containsKey(uid)) {
                    CartItem item = uidToItemMap.get(uid);
                    this.addToCart(item.getName(), item.getImagePath(), item.getPrice());
                    System.out.println("[NFC] 장바구니 추가: " + item.getName());
                } else {
                    showAlert("등록되지 않은 UID입니다.");
                    return;
                }
                switchToPaymentView();
                uidField.clear();
            } else if (event.getCode() == KeyCode.SPACE) {
                handleVoiceTest(); // 음성인식 버튼 실행 *
            }
        });

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String uid = parts[0].trim();
                    String name = parts[1].trim();
                    String image = parts[2].trim();
                    int price = Integer.parseInt(parts[3].trim());

                    uidToItemMap.put(uid, new CartItem(name, image, price));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("상품 파일을 불러오는 데 실패했습니다.");
        }
    }

    private void addToCart(String name, String imagePath, int price) {
        for (CartItem it : MenuController.cartItems) {
            if (it.getName().equals(name)) {
                it.increaseQuantity();
                System.out.println("[장바구니] 수량 증가: " + name);
                return;
            }
        }
        MenuController.cartItems.add(new CartItem(name, imagePath, price));
        System.out.println("[장바구니] 새 아이템 추가: " + name);
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
        boolean received = false;
        try {
            File scriptFile = Paths.get(
                    "JavaFX_Store_Takeout_Template", "src", "app", "play_voice.py").toFile();
            String scriptPath = scriptFile.getAbsolutePath();

            System.out.println("▶ handleVoiceTest() called");
            System.out.println("▶ scriptPath=" + scriptPath);
            System.out.println("▶ script exists: " + scriptFile.exists());

            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                parseOrderLine(line);
                received = true;
            }
            if (received) {
                Platform.runLater(() -> switchToPaymentView());
            } else {
                System.out.println("Python으로부터 출력이 없었습니다.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("알림");
                alert.setHeaderText("음성 인식 실패");
                alert.setContentText("파이썬으로부터 결과를 받지 못했습니다.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("오류");
            errorAlert.setHeaderText("음성 인식 실패");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void switchToPaymentView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/payment.fxml"));
            Stage stage = (Stage) voiceTestButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void parseOrderLine(String line) {
        if (line == null || !line.trim().startsWith("{") || !line.contains("\"menu\"")) {
            return;
        }
        try {
            int m1 = line.indexOf("\"menu\":\"") + 8;
            int m2 = line.indexOf('"', m1);
            int q1 = line.indexOf("\"qty\":", m2) + 6;
            int q2 = line.indexOf('}', q1);

            String menu = line.substring(m1, m2);
            int qty = Integer.parseInt(line.substring(q1, q2).trim());

            Platform.runLater(() -> MenuController.addToCart(menu, qty));
            System.out.println("[VOICE] Parsed → menu: " + menu + ", qty: " + qty);
        } catch (Exception ignore) {
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("알림");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
package app;

import static app.MenuController.cartItems;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CartController {

    @FXML
    private Button closeButton;
    @FXML
    private VBox cartVBox;
    @FXML
    private Button nextButton;

    @FXML
    private TextField uidField; // ✅ 추가

    // 클래스 멤버로 선언되어 있어야 합니다:
    private final Map<String, CartItem> uidToItemMap = new HashMap<>();

    @FXML
    public void initialize() {
        loadItemsFromFile("coffees.txt");
        // 장바구니에 담긴 아이템들 UI로 표시
        for (CartItem item : MenuController.cartItems) {
            HBox card = new HBox(15);
            card.setStyle("-fx-padding: 30px; -fx-background-color: #eeeeee; -fx-background-radius: 30px;");

            // 이미지
            ImageView imageView = new ImageView();
            try {
                imageView.setImage(new Image(getClass().getResource(item.getImagePath()).toExternalForm()));
            } catch (Exception e) {
                System.out.println("이미지를 불러올 수 없습니다: " + item.getImagePath());
            }
            imageView.setFitWidth(90);
            imageView.setFitHeight(90);

            // 메뉴 이름
            Label nameLabel = new Label(item.getName());
            nameLabel.setStyle("-fx-font-size: 28px;  -fx-font-family: 'NanumGothic';");

            // 수량 조절 버튼 및 라벨
            Button minus = new Button("-");
            Label countLabel = new Label(String.valueOf(item.getQuantity())); // 장바구니에서 수량조절하는대로 숫자 바뀜 1로 고정된 거 x
            countLabel.setStyle("-fx-font-size: 28px;  -fx-font-family: 'NanumGothic';");
            Button plus = new Button("+");
            minus.setStyle("-fx-font-size: 22px; -fx-font-family: 'NanumGothic';");
            plus.setStyle("-fx-font-size: 22px; -fx-font-family: 'NanumGothic';");

            // 삭제버튼
            Button deleteBtn = new Button("X");
            deleteBtn.setStyle("-fx-font-size: 22px; -fx-text-fill: red; -fx-font-family: 'NanumGothic';");
            deleteBtn.setOnAction(e -> {
                cartItems.remove(item); // 장바구니에서 제거
                cartVBox.getChildren().remove(card); // UI에서 메뉴 카드 제거
            });

            minus.setOnAction(ev -> { // 없애기
                item.decreaseQuantity(); // 실제 cartItems 리스트에 반영
                countLabel.setText(String.valueOf(item.getQuantity())); // 라벨 갱신
            });

            plus.setOnAction(ev -> { // 추가
                item.increaseQuantity(); // 실제 cartItems 리스트에 반영
                countLabel.setText(String.valueOf(item.getQuantity())); // 라벨 갱신
            });

            HBox quantityBox = new HBox(15, minus, countLabel, plus, deleteBtn);

            VBox infoBox = new VBox(15, nameLabel, quantityBox);
            card.getChildren().addAll(imageView, infoBox);

            cartVBox.getChildren().add(card);
        }

        // 닫기 버튼 누르면 메뉴 화면으로 전환
        closeButton.setOnAction(e -> switchToMenuView());
        nextButton.setOnAction(e -> switchToPaymentView());
    }

    private void loadItemsFromFile(String filename) {

        // ✅ UID 입력 처리
        uidField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String uid = uidField.getText().trim();
                if (uidToItemMap.containsKey(uid)) {
                    CartItem item = uidToItemMap.get(uid);
                    boolean found = false;
                    for (CartItem it : cartItems) {
                        if (it.getName().equals(item.getName())) {
                            it.increaseQuantity();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        cartItems.add(new CartItem(item.getName(), item.getImagePath(), item.getPrice()));
                    }
                    System.out.println("[NFC] 장바구니 추가: " + item.getName());
                } else {
                    showAlert("등록되지 않은 UID입니다.");
                    return;
                }
                switchToPaymentView();
                uidField.clear();
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

    private void switchToMenuView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MenuView.fxml"));
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToPaymentView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/payment.fxml"));
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("알림");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
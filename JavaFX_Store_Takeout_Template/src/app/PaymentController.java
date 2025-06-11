package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import DAO.OrderDAO;

public class PaymentController {

    @FXML
    private VBox paymentListBox;
    @FXML
    private Label totalAmountLabel;

    @FXML
    private TextField uidField; // UID 입력 필드

    // 클래스 멤버로 선언되어 있어야 합니다:
    private final Map<String, CartItem> uidToItemMap = new HashMap<>();

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MenuView.fxml"));
            Stage stage = (Stage) totalAmountLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cpClicked() { // 카드결제
        System.out.println(" 카드 결제 시작 ");
        processPayment();
    }

    @FXML
    private void spClicked() {// 간편결제
        System.out.println(" 간편 결제 시작 ");
        processPayment();
    }

    // 결제 진행 함수(DB에 저장, orderhistory와 orderDetail에 저장장)
    private void processPayment() {
        try {
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.insertOrderFromCart(MenuController.cartItems); // DB 저장

            // 결제 완료 알림
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("결제 완료");
            alert.setHeaderText(null);
            alert.setContentText("결제가 완료되었습니다!");
            alert.showAndWait();

            // 장바구니 초기화
            MenuController.cartItems.clear();

            // 메인 메뉴 화면으로 전환
            Parent root = FXMLLoader.load(getClass().getResource("/MenuView.fxml"));
            Stage stage = (Stage) totalAmountLabel.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("결제 실패");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("결제 중 오류가 발생했습니다.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        int totalAmount = 0;
        paymentListBox.getChildren().clear();

        loadItemsFromFile("coffees.txt");

        for (CartItem item : MenuController.cartItems) { // db
            Pane itemRow = new Pane();
            itemRow.setPrefWidth(412);
            itemRow.setPrefHeight(40);

            Label nameLabel = new Label(item.getName()); // 메뉴이름
            nameLabel.setLayoutX(22);
            nameLabel.setLayoutY(7);
            nameLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'NanumGothic'; -fx-text-fill: black;");

            Label quantityLabel = new Label(item.getQuantity() + "개"); // 개수가격
            quantityLabel.setLayoutX(504);
            quantityLabel.setLayoutY(9);
            quantityLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'NanumGothic';");

            Label priceLabel = new Label((item.getQuantity() * item.getPrice()) + "원"); // 최종합계가격
            priceLabel.setLayoutX(823);
            priceLabel.setLayoutY(10);
            priceLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'NanumGothic';");

            // ➖ 버튼
            Button minusBtn = new Button("-");
            minusBtn.setLayoutX(460);
            minusBtn.setLayoutY(7);
            minusBtn.setStyle("-fx-font-size: 17px; -fx-font-family: 'NanumGothic';");
            minusBtn.setOnAction(e -> {
                item.decreaseQuantity();
                quantityLabel.setText(item.getQuantity() + "개");
                priceLabel.setText((item.getQuantity() * item.getPrice()) + "원");
                updateTotalAmount();
            });

            // ➕ 버튼
            Button plusBtn = new Button("+");
            plusBtn.setLayoutX(560);
            plusBtn.setLayoutY(7);
            plusBtn.setStyle("-fx-font-size: 17px; -fx-font-family: 'NanumGothic';");
            plusBtn.setOnAction(e -> {
                item.increaseQuantity();
                quantityLabel.setText(item.getQuantity() + "개");
                priceLabel.setText((item.getQuantity() * item.getPrice()) + "원");
                updateTotalAmount();
            });

            itemRow.getChildren().addAll(nameLabel, minusBtn, quantityLabel, plusBtn, priceLabel);
            paymentListBox.getChildren().add(itemRow);

            updateTotalAmount();
        }
    }

    private void loadItemsFromFile(String filename) {
        // ✅ UID 입력 처리
        uidField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String uid = uidField.getText().trim();
                if (uidToItemMap.containsKey(uid)) {
                    CartItem item = uidToItemMap.get(uid);
                    boolean found = false;
                    for (CartItem it : MenuController.cartItems) {
                        if (it.getName().equals(item.getName())) {
                            it.increaseQuantity();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        MenuController.cartItems
                                .add(new CartItem(item.getName(), item.getImagePath(), item.getPrice()));
                    }
                    System.out.println("[NFC] 장바구니 추가: " + item.getName());
                } else {
                    showAlert("등록되지 않은 UID입니다.");
                }
                uidField.clear();
                updateTotalAmount();
                initialize();
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

    private void updateTotalAmount() {
        int total = 0;
        for (CartItem item : MenuController.cartItems) {
            total += item.getQuantity() * item.getPrice();
        }
        totalAmountLabel.setText(String.format("%,d원", total));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("알림");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
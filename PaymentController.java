package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentController {

    @FXML private VBox paymentListBox;
    @FXML private Label totalAmountLabel;

    @FXML private void goBack() {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/MenuView.fxml"));
        Stage stage = (Stage) totalAmountLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
    } catch (Exception e) {
        e.printStackTrace();
        }
    }

    @FXML private void cpClicked() { // 카드결제
    System.out.println(" 카드 결제 시작 ");
     // 예: 결제 완료 알림창
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("결제 완료");
    alert.setHeaderText(null);
    alert.setContentText("카드 결제가 완료되었습니다!");
    alert.showAndWait();
    }

    @FXML private void spClicked() {
    System.out.println(" 간편 결제 시작 "); } // 간편결제

    @FXML public void initialize() {
    int totalAmount = 0;

    for (CartItem item : MenuController.cartItems) { // db
    Pane itemRow = new Pane();
    itemRow.setPrefWidth(412);
    itemRow.setPrefHeight(40);

    Label nameLabel = new Label(item.getName()); // 메뉴이름
    nameLabel.setLayoutX(22);
    nameLabel.setLayoutY(7);
    nameLabel.setStyle("-fx-font-size: 19px; -fx-font-family: 'NanumGothic'; -fx-text-fill: black;");

    Label quantityLabel = new Label(item.getQuantity() + "개"); // 개수가격
    quantityLabel.setLayoutX(234);
    quantityLabel.setLayoutY(9);
    quantityLabel.setStyle("-fx-font-size: 19px; -fx-font-family: 'NanumGothic';");

    Label priceLabel = new Label((item.getQuantity() * item.getPrice()) + "원"); // 최종합계가격
    priceLabel.setLayoutX(303);
    priceLabel.setLayoutY(10);
    priceLabel.setStyle("-fx-font-size: 19px; -fx-font-family: 'NanumGothic';");

    // ➖ 버튼
    Button minusBtn = new Button("-");
    minusBtn.setLayoutX(200);
    minusBtn.setLayoutY(7);
    minusBtn.setStyle("-fx-font-size: 12px; -fx-font-family: 'NanumGothic';");
    minusBtn.setOnAction(e -> {
        item.decreaseQuantity();
        quantityLabel.setText(item.getQuantity() + "개");
        priceLabel.setText((item.getQuantity() * item.getPrice()) + "원");
        updateTotalAmount();
         });

    // ➕ 버튼
    Button plusBtn = new Button("+");
    plusBtn.setLayoutX(260);
    plusBtn.setLayoutY(7);
    plusBtn.setStyle("-fx-font-size: 12px; -fx-font-family: 'NanumGothic';");
    plusBtn.setOnAction(e -> {
        item.increaseQuantity();
        quantityLabel.setText(item.getQuantity() + "개");
        priceLabel.setText((item.getQuantity() * item.getPrice()) + "원");
        updateTotalAmount();
        });

    itemRow.getChildren().addAll(nameLabel, minusBtn, quantityLabel, plusBtn, priceLabel);
    paymentListBox.getChildren().add(itemRow);
        }
    }

    private void updateTotalAmount() {
    int total = 0;
    for (CartItem item : MenuController.cartItems) {
        total += item.getQuantity() * item.getPrice();
    }
    totalAmountLabel.setText(String.format("%,d원", total));
    }
}
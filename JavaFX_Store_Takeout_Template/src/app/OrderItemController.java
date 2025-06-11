package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrderItemController {
    @FXML private Label menuNameLabel;
    @FXML private Label quantityLabel;
    @FXML private Label priceLabel;
    @FXML private Button plusBtn;
    @FXML private Button minusBtn;

    private int quantity;
    private int unitPrice;

    public void setOrderData(String name, int qty, int price) {
        this.quantity = qty;
        this.unitPrice = price;
        menuNameLabel.setText(name);
        quantityLabel.setText(quantity + "개");
        priceLabel.setText((quantity * unitPrice) + "원");
    }

    @FXML
    private void handleOptionClick(MouseEvent event) {
        System.out.println("옵션 버튼 클릭됨 (메뉴: " + menuNameLabel.getText() + ")");
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/option.fxml"));
        Parent popupRoot = loader.load();

        Stage popupStage = new Stage();
        popupStage.setTitle("옵션 선택");
        popupStage.setScene(new Scene(popupRoot));
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        plusBtn.setOnAction(e -> {
            quantity++;
            updateDisplay();
        });
        minusBtn.setOnAction(e -> {
            if (quantity > 1) quantity--;
            updateDisplay();
        });
    }

    private void updateDisplay() {
        quantityLabel.setText(quantity + "개");
        priceLabel.setText((quantity * unitPrice) + "원");
    }
}
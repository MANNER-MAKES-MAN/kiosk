/* package app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class PayController {

    @FXML
    private AnchorPane orderPane;

    private int totalAmount = 0;

    public void setOrderItems(List<CartItem> orderItems) {
        totalAmount = 0;
        int yOffset = 52;

        for (CartItem item : orderItems) {
            int itemTotal = item.getPrice() * item.getQuantity();
            totalAmount += itemTotal;

            Pane itemRow = new Pane();
            itemRow.setLayoutX(10);
            itemRow.setLayoutY(yOffset);
            itemRow.setPrefWidth(412);
            itemRow.setPrefHeight(40);

            Label nameLabel = new Label(item.getName());
            nameLabel.setLayoutX(22);
            nameLabel.setLayoutY(7);
            nameLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'NanumGothic'; -fx-text-fill: black;");

            Label quantityLabel = new Label(item.getQuantity() + "개");
            quantityLabel.setLayoutX(234);
            quantityLabel.setLayoutY(9);
            quantityLabel.setPrefWidth(41);
            quantityLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'NanumGothic'; -fx-text-fill: black;");

            Label priceLabel = new Label(itemTotal + "원");
            priceLabel.setLayoutX(303);
            priceLabel.setLayoutY(10);
            priceLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'NanumGothic'; -fx-text-fill: black;");

            itemRow.getChildren().addAll(nameLabel, quantityLabel, priceLabel);
            orderPane.getChildren().add(itemRow);

            yOffset += 45;
        }

        for (javafx.scene.Node node : orderPane.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                if ("10,200원".equals(label.getText())) {
                    label.setText(totalAmount + "원");
                    break;
                }
            }
        }
    }
}
*/
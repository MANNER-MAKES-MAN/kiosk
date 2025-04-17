package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MenuController {

    @FXML private Button backButton;
    @FXML private Button cartButton;
    @FXML private Button nextButton;
    @FXML private Button addIcecoffee;
    @FXML private Button addHotcoffee;

    public static List<CartItem> cartItems = new ArrayList<>();

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> switchScene("/TypeSelect.fxml"));
        cartButton.setOnAction(e -> switchScene("/CartView.fxml"));
        nextButton.setOnAction(e -> switchScene("/Option.fxml"));

        addIcecoffee.setOnAction(e -> {
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getName().equals("ICE 아메리카노")) {
                    item.increaseQuantity();
                    found = true;
                    break;
                }
            }
            if (!found) {
                cartItems.add(new CartItem("ICE 아메리카노", "/icecoffee.png"));
            }
            System.out.println("장바구니에 추가됨: ICE 아메리카노");
        });

    }


    private void switchScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

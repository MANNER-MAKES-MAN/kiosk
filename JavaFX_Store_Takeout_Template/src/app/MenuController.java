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
    @FXML private Button addIcelatte;
    @FXML private Button addHotlatte;
    @FXML private Button addIceBlatte;
    @FXML private Button addHotBlatte;

    public static List<CartItem> cartItems = new ArrayList<>();

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> switchScene("/TypeSelect.fxml"));
        cartButton.setOnAction(e -> switchScene("/CartView.fxml"));
        nextButton.setOnAction(e -> switchScene("/payment.fxml"));

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
        }); // 아아

        addHotcoffee.setOnAction(e -> {
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getName().equals("HOT 아메리카노")) {
                    item.increaseQuantity();
                    found = true;
                    break;
                }
            }
            if (!found) {
                cartItems.add(new CartItem("HOT 아메리카노", "/hotcoffee.jpg"));
            }
            System.out.println("장바구니에 추가됨: HOT 아메리카노");
        }); // 뜨아

        addIcelatte.setOnAction(e -> {
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getName().equals("ICE 카페라떼")) {
                    item.increaseQuantity();
                    found = true;
                    break;
                }
            }
            if (!found) {
                cartItems.add(new CartItem("ICE 카페라떼", "/icelatte.png"));
            }
            System.out.println("장바구니에 추가됨: ICE 카페라떼");
        }); // 아라

        addHotlatte.setOnAction(e -> {
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getName().equals("HOT 카페라떼")) {
                    item.increaseQuantity();
                    found = true;
                    break;
                }
            }
            if (!found) {
                cartItems.add(new CartItem("HOT 카페라떼", "/hotlatte.jpeg"));
            }
            System.out.println("장바구니에 추가됨: HOT 카페라떼");
        }); // 뜨라

        addIceBlatte.setOnAction(e -> {
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getName().equals("ICE 바닐라라떼")) {
                    item.increaseQuantity();
                    found = true;
                    break;
                }
            }
            if (!found) {
                cartItems.add(new CartItem("ICE 바닐라라떼", "/iceBlatte.png"));
            }
            System.out.println("장바구니에 추가됨: ICE 바닐라라떼");
        }); // 아바라

        addHotBlatte.setOnAction(e -> {
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getName().equals("HOT 바닐라라떼")) {
                    item.increaseQuantity();
                    found = true;
                    break;
                }
            }
            if (!found) {
                cartItems.add(new CartItem("HOT 바닐라라떼", "/hotBlatte.jpeg"));
            }
            System.out.println("장바구니에 추가됨: HOT 바닐라라떼");
        }); // 뜨바라


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

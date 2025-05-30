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

        addIcecoffee.setOnAction(e -> { addToCart ("ICE 아메리카노", "/icecoffee.png", 1500);}); // 아아

        addHotcoffee.setOnAction(e -> { addToCart ("HOT 아메리카노", "/hotcoffee.jpg", 1500 );}); // 뜨아

        addIcelatte.setOnAction(e -> { addToCart ("ICE 카페라떼", "/icelatte.png", 2900 );}); // 아라

        addHotlatte.setOnAction(e -> { addToCart("HOT 카페라떼", "/hotlatte.jpeg", 2900);}); // 뜨라

        addIceBlatte.setOnAction(e -> { addToCart("ICE 바닐라라떼", "/iceBlatte.png", 2900); }); // 아바라

        addHotBlatte.setOnAction(e -> { addToCart("HOT 바닐라라떼", "/hotBlatte.jpeg",2900);}); // 뜨바라
    }

    private void addToCart(String name, String imagePath, int price) {
    for (CartItem item : cartItems) {
        if (item.getName().equals(name)) {
            item.increaseQuantity();
            System.out.println("수량 증가됨: " + name); // 똑같은 메뉴를 한 번 더 눌렀을 때 '수량 증가됨:menu' 출력
            return;
        }
    }
    cartItems.add(new CartItem(name, imagePath, price));
    System.out.println("장바구니에 새로 추가됨: " + name);
    } // 메뉴 클릭하면 장바구니에 추가

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

package app;

import static app.MenuController.cartItems;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CartController {

    @FXML private Button closeButton;
    @FXML private VBox cartVBox;
    @FXML private Button nextButton;

    @FXML
    public void initialize() {
        // 장바구니에 담긴 아이템들 UI로 표시
        for (CartItem item : MenuController.cartItems) {
            HBox card = new HBox(10);
            card.setStyle("-fx-padding: 10px; -fx-background-color: #eeeeee; -fx-background-radius: 10px;");

            // 이미지
            ImageView imageView = new ImageView();
            try {
                imageView.setImage(new Image(getClass().getResource(item.getImagePath()).toExternalForm()));
            } catch (Exception e) {
                System.out.println("이미지를 불러올 수 없습니다: " + item.getImagePath());
            }
            imageView.setFitWidth(60);
            imageView.setFitHeight(60);

            // 메뉴 이름
            Label nameLabel = new Label(item.getName());
            nameLabel.setStyle("-fx-font-size: 18px;  -fx-font-family: 'NanumGothic';");

            // 수량 조절 버튼 및 라벨
            Button minus = new Button("-");
            Label countLabel = new Label(String.valueOf(item.getQuantity())) ; // 장바구니에서 수량조절하는대로 숫자 바뀜 1로 고정된 거 x
            countLabel.setStyle("-fx-font-size: 12px;  -fx-font-family: 'NanumGothic';");
            Button plus = new Button("+");


            // 삭제버튼
            Button deleteBtn = new Button("X");
            deleteBtn.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-font-family: 'NanumGothic';");
            deleteBtn.setOnAction(e -> {
            cartItems.remove(item); // 장바구니에서 제거
            cartVBox.getChildren().remove(card); // UI에서 메뉴 카드 제거
            });



            minus.setOnAction(ev -> { // 없애기
                int count = Integer.parseInt(countLabel.getText());
                if (count > 0) {
                    countLabel.setText(String.valueOf(--count));
                }
            });

            plus.setOnAction(ev -> { // 추가
                int count = Integer.parseInt(countLabel.getText());
                countLabel.setText(String.valueOf(++count));
            });

            HBox quantityBox = new HBox(5, minus, countLabel, plus, deleteBtn);

            VBox infoBox = new VBox(5, nameLabel, quantityBox);
            card.getChildren().addAll(imageView, infoBox);

            cartVBox.getChildren().add(card);
        }

        // 닫기 버튼 누르면 메뉴 화면으로 전환
        closeButton.setOnAction(e -> switchToMenuView());
        nextButton.setOnAction(e -> switchToPaymentView());
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
}

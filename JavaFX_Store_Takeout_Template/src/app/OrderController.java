package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Optional;

import static app.MenuController.cartItems;

public class OrderController {
    @FXML
    private Label quantityLabel;
    @FXML
    private Pane cardPayment, simplePayment;

    private int quantity = 1;

    @FXML
    private void handleDecrease(MouseEvent event) {
        if (quantity > 1) {
            quantity--;
            updateQuantityLabel();
        }
    }

    @FXML
    private void handleIncrease(MouseEvent event) {
        quantity++;
        updateQuantityLabel();
    }

    private void updateQuantityLabel() {
        quantityLabel.setText(quantity + "개");
    }

    @FXML
    private void handleOptionClick(MouseEvent event) {
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
    private void cpClicked(MouseEvent event) {
        setSelectedStyle(cardPayment);
        resetStyle(simplePayment);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("카드결제");
        alert.setHeaderText(null);
        alert.setContentText("카드결제를 하시겠습니까?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            sendPaymentRequest("card");
        } else {
            System.out.println("결제 취소");
        }
    }

    @FXML
    private void spClicked(MouseEvent event) {
        setSelectedStyle(simplePayment);
        resetStyle(cardPayment);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("간편결제");
        alert.setHeaderText(null);
        alert.setContentText("간편결제를 하시겠습니까?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            sendPaymentRequest("simple");
        } else {
            System.out.println("결제 취소");
        }
    }

    private void setSelectedStyle(Pane pane) {
        pane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
    }

    private void resetStyle(Pane pane) {
        pane.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
    }

    private void sendPaymentRequest(String method) {
        try {
            URI uri = URI.create("http://127.0.0.1:5000/pay");
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String data = "amount=10200&method=" + method;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("결제 성공");
                successAlert.setHeaderText(null);
                successAlert.setContentText("결제가 완료되었습니다!");
                successAlert.showAndWait();
            } else {
                Alert failAlert = new Alert(Alert.AlertType.ERROR);
                failAlert.setTitle("결제 실패");
                failAlert.setHeaderText(null);
                failAlert.setContentText("결제에 실패했습니다. 다시 시도해주세요.");
                failAlert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("결제 오류");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("서버 연결에 실패했습니다.");
            errorAlert.showAndWait();
        }
    }
}
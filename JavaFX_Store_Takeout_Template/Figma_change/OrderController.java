package Figma_change;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import DAO.Menu;
import DAO.MenuDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import Other.TTS;

public class OrderController {
    @FXML
    private Label quantityLabel; // FXML에서 정의된 Label과 연결

    private int quantity = 1;

    // 갯수 늘리고 줄이는 컨트롤
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

    // 옵션 창 여는 컨트롤
    @FXML
    private void handleOptionClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("option.fxml"));
            Parent popupRoot = loader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle("옵션 선택");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL); // 모달창으로 만들기 (선택 사항)
            popupStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 카드 결제 간편 결제 클릭 컨트롤러
    @FXML
    private Pane cardPayment, simplePayment;

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
            System.out.println("카드결제 진행");
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
            System.out.println("간편결제 진행");
            // 여기에 결제 로직 넣기
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
}

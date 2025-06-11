package app;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class OptionController {

    @FXML
    private Pane shortChoice, tallChoice, grandeChoice, ventiChoice;
    @FXML
    private Label shortLabel, tallLabel, grandeLabel, ventiLabel;

    @FXML
    private void sizeClicked(MouseEvent event) {
        Object source = event.getSource();
        Pane selectedPane = null;
        Label selectedLabel = null;

        // 어떤 Pane이나 Label이 클릭되었는지 확인
        if (source == shortChoice || source == shortLabel) {
            selectedPane = shortChoice;
            selectedLabel = shortLabel;
        } else if (source == tallChoice || source == tallLabel) {
            selectedPane = tallChoice;
            selectedLabel = tallLabel;
        } else if (source == grandeChoice || source == grandeLabel) {
            selectedPane = grandeChoice;
            selectedLabel = grandeLabel;
        } else if (source == ventiChoice || source == ventiLabel) {
            selectedPane = ventiChoice;
            selectedLabel = ventiLabel;
        }
        if (selectedPane != null && selectedLabel != null) {
            // 모든 Pane, Label 초기화
            resetStyle(shortChoice, shortLabel);
            resetStyle(tallChoice, tallLabel);
            resetStyle(grandeChoice, grandeLabel);
            resetStyle(ventiChoice, ventiLabel);

            // 선택된 Pane, Label 스타일 적용
            setSelectedStyle(selectedPane, selectedLabel);
        }
    }

    private void resetStyle(Pane pane, Label label) {
        pane.setStyle(
                "-fx-background-color: #FFFFFF; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-color: #D9D9D9; -fx-border-width: 1;");

        // ImageView 투명도 조정
        pane.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                ((ImageView) node).setOpacity(0.5);
            }
        });

        // Label 색 회색으로
        label.setStyle("-fx-text-fill: #D9D9D9; -fx-font-family: 'Inika'; -fx-font-size: 14px;");
    }

    private void setSelectedStyle(Pane pane, Label label) {
        pane.setStyle(
                "-fx-background-color: #D9D9D9; -fx-background-radius: 30; -fx-border-radius: 30; -fx-border-color: black; -fx-border-width: 1;");

        // ImageView 투명도 복원
        pane.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                ((ImageView) node).setOpacity(1.0);
            }
        });

        // Label 색 검정으로
        label.setStyle("-fx-text-fill: #4F4F4F; -fx-font-family: 'Inika'; -fx-font-size: 14px;");
    }

    @FXML
    private Pane sugarChoice, milkChoice;
    @FXML
    private Pane omChoice, smChoice, amChoice;
    @FXML
    private Pane milkType; // milkType 영역 전체

    @FXML
    private void extraClicked(MouseEvent event) {
        Pane clickedPane = (Pane) event.getSource();

        // 전체 초기화
        initStyle(sugarChoice);
        initStyle(milkChoice);

        // milkType 하위 옵션들 비활성화
        setMilkTypeEnabled(false);

        if (clickedPane == sugarChoice) {
            selectedStyle(sugarChoice);
        } else if (clickedPane == milkChoice) {
            selectedStyle(milkChoice);
            setMilkTypeEnabled(true); // milkChoice 선택 시 milkType 옵션들 활성화
        }
    }

    private void initStyle(Pane pane) {
        pane.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-background-radius: 30; " +
                        "-fx-border-radius: 30; " +
                        "-fx-border-color: #D9D9D9; " +
                        "-fx-border-width: 1;");

        pane.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setStyle(
                        "-fx-font-size: 14px; " +
                                "-fx-font-family: 'Inika'; " +
                                "-fx-text-fill: #D9D9D9;");
            }
        });
    }

    private void selectedStyle(Pane pane) {
        pane.setStyle(
                "-fx-background-color: #D9D9D9; " +
                        "-fx-background-radius: 30; " +
                        "-fx-border-radius: 30; " +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 1;");

        pane.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setStyle(
                        "-fx-font-size: 14px; " +
                                "-fx-font-family: 'Inika'; " +
                                "-fx-text-fill: #4F4F4F;" // 진한 회색 (선택된 상태)
                );
            }
        });
    }

    @FXML
    private void milkTypeClicked(MouseEvent event) {
        Pane clickedPane = (Pane) event.getSource();

        initStyle(omChoice);
        initStyle(smChoice);
        initStyle(amChoice);

        selectedStyle(clickedPane);
    }

    private void setMilkTypeEnabled(boolean enabled) {
        omChoice.setDisable(!enabled);
        smChoice.setDisable(!enabled);
        amChoice.setDisable(!enabled);

        // 비활성화 시 스타일도 초기화
        if (!enabled) {
            initStyle(omChoice);
            initStyle(smChoice);
            initStyle(amChoice);
        }
    }

    @FXML
    private Button goBackButton;

    @FXML
    private void gbClicked(ActionEvent event) {
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        stage.close();
    }

}

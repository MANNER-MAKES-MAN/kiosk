package app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import DAO.Menu;
import DAO.MenuDAO;
import Other.TTS;

public class TypeSelectController {

    @FXML private Button storeButton;
    @FXML private Button takeoutButton;
    @FXML private Button voiceTestButton;

    @FXML
    public void initialize() {
        storeButton.setOnAction(e -> switchToMenu());
        takeoutButton.setOnAction(e -> switchToMenu());

        voiceTestButton.setOnAction(e -> handleVoiceTest());
    }

    private void switchToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MenuView.fxml"));
            Stage stage = (Stage) storeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleVoiceTest() {
        try {
            // 파이썬 스크립트 경로 설정
            ProcessBuilder pb = new ProcessBuilder("python", "C:\\Voice_test\\voice_test.py");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 결과 표시
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("음성 인식 결과");
            alert.setHeaderText("음성 인식 결과");
            alert.setContentText(output.toString());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("오류");
            errorAlert.setHeaderText("음성 인식 실패");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }
    @FXML
    private void voiceClicked(MouseEvent event) {
    	try {
//    		 // 음성 인식 파이썬 스크립트 실행(일단 주석 처리함)
//            ProcessBuilder pb = new ProcessBuilder("python", "voice_test.py");
//            pb.redirectErrorStream(true);
//            Process process = pb.start();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder result = new StringBuilder();
//            String line;
    		
    		
            // 1. 음성 인식 결과 받아오기 (테스트용 임시 텍스트 사용)
            String recognizedText = "아메리카노 한잔";  // 예: 음성인식 결과
            System.out.println("인식된 텍스트: " + recognizedText);

            // 2. 단어 단위로 분해 (공백 기준)
            String[] tokens = recognizedText.split("\\s+");

            // 3. DAO를 통해 메뉴 이름 목록 가져오기
            MenuDAO dao = new MenuDAO();
            List<Menu> allMenus = dao.selectAll();
            Set<String> menuNames = new HashSet<>();
            for (Menu menu : allMenus) {
                menuNames.add(menu.getName());  // 정확히 일치하는 이름만 추가
            }

            // 4. 메뉴 이름 판별 (가장 먼저 일치하는 단어를 메뉴로 간주)
            String matchedMenuName = null;
            for (String token : tokens) {
                if (menuNames.contains(token)) {
                    matchedMenuName = token;
                    break;
                }
            }

            // 5. 음성 파일 리스트 준비
            List<String> audioFiles = new ArrayList<>();

            if (matchedMenuName != null) {
                // 5-1. 메뉴 이름에 해당하는 음성파일명(DB에서) 조회
                String menuAudioFile = dao.getAudioGuideByName(matchedMenuName);
                if (menuAudioFile != null) {
                    audioFiles.add(menuAudioFile); // 예: menu000000001.wav
                }
            }

            // 5-2. 메뉴 이름을 제외한 나머지 단어 → 개별 음성파일로 재생
            for (String token : tokens) {
                if (!token.equals(matchedMenuName)) {
                    audioFiles.add(token + ".wav"); // 예: "한잔" → "한잔.wav"
                }
            }

            // 6. TTS 모듈에서 순차 재생
            for (String filename : audioFiles) {
                TTS.play(filename);
            }

        } catch (Exception e) {
            System.out.println("음성 처리 중 오류 발생:");
            e.printStackTrace();
        }
    }
}
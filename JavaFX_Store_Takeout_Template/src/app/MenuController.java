package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuController {

    @FXML
    private Button backButton;
    @FXML
    private Button cartButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button addAmericano, addCafeLatte, addIceTea,
            addHotChoco, addAde, addSmoothie;

    @FXML
    private TextField uidField; // ✅ 추가

    /**
     * CartItem 을 보관하는 전역 리스트.
     * 음성/버튼 어떤 경로로 추가되든 모두 이 리스트에 저장됩니다.
     */
    public static final List<CartItem> cartItems = new ArrayList<>();
    private final Map<String, CartItem> uidToItemMap = new HashMap<>();

    /**
     * 음성 인식 결과로 호출되는 메서드
     * 
     * @param name 메뉴 이름 (예: "아메리카노")
     * @param qty  수량 (예: 4)
     */
    public static void addToCart(String name, int qty) {
        // 1) 음성으로 넘어온 name 에 해당하는 이미지 경로와 가격을 결정
        String imgPath;
        int price;

        switch (name) {
            case "아메리카노":
                imgPath = "/icecoffee.png";
                price = 1500;
                break;
            case "카페라떼":
                imgPath = "/icelatte.png";
                price = 1500;
                break;
            case "아이스티":
                imgPath = "/IceTea.png";
                price = 2900;
                break;
            case "핫초코":
                imgPath = "/HotChoco.jpg";
                price = 2900;
                break;
            case "에이드":
                imgPath = "/Ade.jpg";
                price = 2900;
                break;
            case "스무디":
                imgPath = "/Smoothie.jpg";
                price = 2900;
                break;
            default:
                // 예외 처리: 등록되지 않은 메뉴가 들어왔을 때
                System.out.println("[VOICE] 알 수 없는 메뉴: " + name + " → 무시");
                return;
        }

        // 2) 이미 cartItems 에 같은 이름의 항목이 있으면 수량만 증가
        for (CartItem it : cartItems) {
            if (it.getName().equals(name)) {
                for (int i = 0; i < qty; i++) {
                    it.increaseQuantity();
                }
                System.out.println("[VOICE] 수량 증가: " + name + " +" + qty);
                return;
            }
        }

        // 3) 기존에 같은 이름이 없으면 새 CartItem 을 만들고 qty 만큼 수량 설정
        CartItem newItem = new CartItem(name, imgPath, price);
        // 생성자에서 수량이 1개로 설정되므로, 2번째 아이템부터 qty-1 번 증가시킴
        for (int i = 1; i < qty; i++) {
            newItem.increaseQuantity();
        }
        cartItems.add(newItem);
        System.out.println("[VOICE] 장바구니 추가: " + name + " ×" + qty);
    }

    /**
     * UI 버튼으로 메뉴를 추가할 때 호출되는 메서드.
     * (예: 메뉴 화면에서 버튼을 클릭했을 때)
     * 
     * @param name  메뉴 이름
     * @param img   이미지 파일 경로 (리소스 경로 기준)
     * @param price 개당 가격
     */
    private void addToCartUI(String name, String img, int price) {
        for (CartItem it : cartItems) {
            if (it.getName().equals(name)) {
                it.increaseQuantity();
                System.out.println("[UI] 수량 +1: " + name);
                return;
            }
        }
        cartItems.add(new CartItem(name, img, price));
        System.out.println("[UI] 장바구니 추가: " + name);
    }

    @FXML
    private void initialize() {
        backButton.setOnAction(e -> switchScene("/TypeSelect.fxml"));
        cartButton.setOnAction(e -> switchScene("/CartView.fxml"));
        nextButton.setOnAction(e -> switchScene("/payment.fxml"));

        // UI 버튼에 직접 연결된 addToCartUI 호출
        addAmericano.setOnAction(e -> addToCartUI("아메리카노", "/icecoffee.png", 1500));
        addCafeLatte.setOnAction(e -> addToCartUI("카페라떼", "/icelatte.png", 1500));
        addIceTea.setOnAction(e -> addToCartUI("아이스티", "/iceTea.png", 2900));
        addHotChoco.setOnAction(e -> addToCartUI("핫초코", "/HotChoco.jpg", 2900));
        addAde.setOnAction(e -> addToCartUI("에이드", "/Ade.jpg", 2900));
        addSmoothie.setOnAction(e -> addToCartUI("스무디", "/Smoothie.jpg", 2900));

        loadItemsFromFile("coffees.txt");
    }

    private void loadItemsFromFile(String filename) {

        // ✅ UID 입력 처리
        uidField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String uid = uidField.getText().trim();
                if (uidToItemMap.containsKey(uid)) {
                    CartItem item = uidToItemMap.get(uid);
                    boolean found = false;
                    for (CartItem it : cartItems) {
                        if (it.getName().equals(item.getName())) {
                            it.increaseQuantity();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        cartItems.add(new CartItem(item.getName(), item.getImagePath(), item.getPrice()));
                    }
                    System.out.println("[NFC] 장바구니 추가: " + item.getName());
                } else {
                    showAlert("등록되지 않은 UID입니다.");
                    return;
                }
                switchToPaymentView();
                uidField.clear();
            }
        });

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String uid = parts[0].trim();
                    String name = parts[1].trim();
                    String image = parts[2].trim();
                    int price = Integer.parseInt(parts[3].trim());

                    uidToItemMap.put(uid, new CartItem(name, image, price));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("상품 파일을 불러오는 데 실패했습니다.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("알림");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * FXML 리소스 경로로 화면을 전환하는 헬퍼 메서드
     */
    private void switchScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) backButton.getScene().getWindow();
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
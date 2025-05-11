package DAO;

public class Menu {
    private String menuId;  // 자동 생성이므로 사용 X
    private String name;
    private int price;
    private int stock;
    private String imagePath;
    private String audioGuide;
    private String category;

    // 기본 생성자
    public Menu() {}

    // 필요한 필드만 받는 생성자
    public Menu(String name, int price, String category, String imagePath) {
        this.name = name;
        this.price = price;
        //이미지로 전달받는 것이 비어있으면 null, 아닐 시 설정한 이름을 저장
        this.imagePath = (imagePath == null || imagePath.trim().isEmpty()) ? null : imagePath;
        this.category = category;
    }

    // Getter & Setter 현 시정에서 메뉴 이름과 가격, 카테고리만 사용
    public String getMenuID() { return menuId; }
    public void setMenuID(String menuId) { this.menuId = menuId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public String getAudioGuide() { return audioGuide; }
    public void setAudioGuide(String audioGuide) { this.audioGuide = audioGuide; }

    // ... 필요시 다른 필드 추가 가능
}

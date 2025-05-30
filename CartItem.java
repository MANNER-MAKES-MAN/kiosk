package app;

public class CartItem {
    private String name;
    private String imagePath;
    private int quantity;
    private int price; // ✅ 가격 필드 추가
    public String getQuantity;

    public CartItem(String name, String imagePath, int price) {
        this.name = name;
        this.imagePath = imagePath;
        this.price = price;
        this.quantity = 1;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() { // ✅ getter 추가
        return price;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
          if (quantity > 1) {
        quantity--;
    } else {
        quantity = 0;
    }
    }
}
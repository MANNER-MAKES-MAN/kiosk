package app;

public class CartItem {
    private String name;
    private String imagePath;
    private int quantity;

    public CartItem(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
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

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        if (quantity > 0) quantity--;
    }
}

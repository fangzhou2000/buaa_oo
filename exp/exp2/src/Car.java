public class Car extends Vehicle {
    private int maxSpeed;

    Car(int id, int price, int maxSpeed) {
        // TODO
        super(id, price);
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void run() {
        System.out.println("Wow, I can Run (maxSpeed:" + maxSpeed + ")!");
    }

    @Override
    public int getPrice() {
        // TODO
        int price = super.getPrice();
        if (this.maxSpeed >= 1000) {
            price = price + 1000;
        }
        System.out.println("price is: " + price);
        return price;
    }
}

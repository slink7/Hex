package Hex;

public class App {
    public static void main(String[] args) {
        Board.log("Starting Hex");
        Window window = new Window();
        window.run();
        Board.log("Ending Hex");
    }
}

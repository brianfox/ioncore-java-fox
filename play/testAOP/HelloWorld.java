package testAOP;

public class HelloWorld {

    public static void main(String args[]) {
        HelloWorld world = new HelloWorld();
        world.greet();
    }

    public void greet() {
        System.out.println("Hello World!");
    }
}

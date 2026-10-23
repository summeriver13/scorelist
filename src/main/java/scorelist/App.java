package scorelist;

import java.util.Scanner;

public final class App {

    private App() {
    }

    public static void main(String[] args) {
        new Cli(new Scanner(System.in), System.out).run();
    }
}

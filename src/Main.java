import java.io.*;
import java.util.Scanner;

import java.io.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args)throws Exception {


        Basket basket = new Basket();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Список возможных товаров для покупки");
        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {100, 200, 300};
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + " " + products[i] + " " + prices[i] + " руб/шт");

        }

        File file = new File("basket.txt");
        try {
            if (file.createNewFile() || file.length() == 0) {
                basket = new Basket(products, prices);
            } else {
                basket = Basket.loadFromTxtFile(file);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        int[] productsCount = new int[products.length];
        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String inputString = scanner.nextLine();
            if (inputString.equals("end")) {
                break;
            }
            String[] parts = inputString.split(" ");
            int productNumber = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);

            if (productCount != 0) {
                basket.addToBasket(productNumber, productCount);
            }
            try {
                basket.saveTxt(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        basket.printCart();
    }
}

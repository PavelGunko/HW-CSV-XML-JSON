import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        Basket basket = new Basket();
        ClientLog clientLog = new ClientLog();  //создание объекта созданного класса

        //прайс магазина
        Scanner scanner = new Scanner(System.in);
        System.out.println("Список возможных товаров для покупки");
        String[] products = {"Хлеб", "Яблоки", "Молоко"};
        int[] prices = {100, 200, 300};
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + " " + products[i] + " " + prices[i] + " руб/шт");

        }

        File file = new File("basket.json");
        try {
            if (file.createNewFile() || file.length() == 0) {
                basket = new Basket(products, prices);
            } else {
                basket = Basket.loadFromJson(file);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String inputString = scanner.nextLine();
            if (inputString.equals("end")) {
                clientLog.exportAsCSV(new File("client.csv"));
                break;
            }
            String[] parts = inputString.split(" ");
            int productNumber = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);

            if (productCount != 0) {
                basket.addToBasket(productNumber, productCount);
                clientLog.log(productNumber, productCount);
                //через метод log и с записью в csv

            }
            try {
                basket.saveJson(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        basket.printCart();




    }
}


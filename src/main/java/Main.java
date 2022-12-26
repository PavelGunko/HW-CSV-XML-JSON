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
        String logName = "";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("config.xml"));

            XPath xPath = XPathFactory.newInstance().newXPath();
            boolean inLoadEnabled = Boolean.parseBoolean(xPath
                    .compile("/config/load/enabled")
                    .evaluate(doc));
            boolean inSaveEnabled = Boolean.parseBoolean(xPath
                    .compile("/config/save/enabled")
                    .evaluate(doc));
            boolean logEnabled = Boolean.parseBoolean(xPath
                    .compile("/config/log/enabled")
                    .evaluate(doc));
            String loadFileName = xPath
                    .compile("/config/load/fileName")
                    .evaluate(doc);
            String saveFileName = xPath
                    .compile("/config/save/fileName")
                    .evaluate(doc);
            String loadFormat = xPath
                    .compile("/config/load/format")
                    .evaluate(doc);
            String saveFormat = xPath
                    .compile("/config/save/format")
                    .evaluate(doc);

            String logFileName = xPath
                    .compile("/config/log/fileName")
                    .evaluate(doc);
/*
Блок проверки настройки и загрузка соответсвующих параметров
 */

            if (inLoadEnabled) {
                switch (loadFormat) {
                    case "json":
                        basket = Basket.loadFromJson(new File(loadFileName));
                        break;
                    case "text":
                        basket = Basket.loadFromTxtFile(new File(loadFileName));
                        break;

                }
            }
            if (inSaveEnabled) {

                switch (saveFormat) {
                    case "json":
                        basket.saveJson(new File(saveFileName));
                        break;
                    case "text":
                        basket.saveTxt(new File(saveFileName));
                        break;
                }
            }
            if (logEnabled) {
                logName = logFileName;
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
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
                clientLog.exportAsCSV(new File(logName));
                break;
            }
            String[] parts = inputString.split(" ");
            int productNumber = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);

            if (productCount != 0) {
                basket.addToBasket(productNumber, productCount);
                clientLog.log(productNumber, productCount); //через метод log и с записью в csv

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


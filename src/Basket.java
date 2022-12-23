import java.io.*;
import java.util.Scanner;

public class Basket {

    protected String[] names;
    protected int[] prices;
    protected int[] amounts;


    //конструктор, принимающий массив цен и названий продуктов;
    public Basket(String[] names, int[] prices) {
        this.names = names;
        this.prices = prices;
        this.amounts = new int[names.length];


    }

    //конструтор по умолчанию для сериализации, для Bin должен быть private
    public Basket() {

    }

    //метод добавления amount штук продукта номер productNum в корзину;
    public void addToBasket(int productNum, int amount) {
        amounts[productNum] += amount;
    }

    //вывод карзины
    public void printCart() {
        int sumProducts = 0;        //итоговая сумма корзины
        System.out.println("Ваша корзина: ");
        for (int i = 0; i < names.length; i++) {
            if (amounts[i] > 0) {
                System.out.println(names[i] + " " + amounts[i] + " шт. "
                        + prices[i] + "руб./шт. " + amounts[i] * prices[i] + " руб. в сумме");
            }
            sumProducts += amounts[i] * prices[i];
        }
        System.out.println("Итого: " + sumProducts + " руб.");
    }

    //метод сохранения корзины в текстовый файл;
    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(textFile);)
        {
            writer.println(names.length);
            for (int i = 0; i < names.length; i++) {
                writer.println(names[i] + "\t" + prices[i] + "\t" + amounts[i]);

            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /*статический(!) метод восстановления объекта корзины из текстового
     файла, в который ранее была она сохранена;*/
    public static Basket loadFromTxtFile(File textFile) throws IOException {
        String[] names = null;
        int[] prices = null;
        int[] amounts = null;
        Basket basket=null;
        try (Scanner scanner = new Scanner(new FileInputStream(textFile));) {
            int size = Integer.parseInt(scanner.nextLine());
            names = new String[size];
            prices = new int[size];
            amounts = new int[size];

            for (int i = 0; i < size; i++) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
                names[i] = parts[0];
                prices[i] = Integer.parseInt(parts[1]);
                amounts[i] = Integer.parseInt(parts[2]);

            }
            basket = new Basket(names, prices);
            for (int i = 0; i < names.length; i++) {
                basket.addToBasket(i, amounts[i]); //через конструктор
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return basket;


    }

}
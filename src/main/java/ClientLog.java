import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
   List<String> basket = new ArrayList<>();

   public void log(int productNum, int amount) {
        basket.add((productNum + 1) + "," + (amount));
    }

    public void exportAsCSV(File file)
    {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true),
                ',',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)){
            basket.forEach(s -> csvWriter.writeNext(s.split(",")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
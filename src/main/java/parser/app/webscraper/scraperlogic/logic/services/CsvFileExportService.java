package parser.app.webscraper.scraperlogic.logic.services;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.FileExportService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CsvFileExportService implements FileExportService {

    @Override
    public void exportData(List<String> header, Map<String, List<String>> allPagesParseResult, String pathToOutput) {
        FileWriter outputFile = null;
        do {
            System.out.println("Введите путь к сохранению файла: ");
            File file = new File(pathToOutput);
            try {
                outputFile = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Неверный путь к файлу");
            }
        } while (outputFile == null);

        CSVWriter writer = new CSVWriter(outputFile, ';');
        fillHeader(writer, header);
        writeData(writer, allPagesParseResult);

        try {
            writer.close();
            outputFile.close();
            System.out.println("Файл успешно сохранен по пути: " + pathToOutput);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при закрытии потоков записи.");
        }
    }

    private void fillHeader(CSVWriter writer, List<String> header) {
        writer.writeNext(header.toArray(String[]::new));
    }

    private void writeData(CSVWriter writer, Map<String, List<String>> allPagesParseResult) {
        int i = 0;
        for (Map.Entry<String, List<String>> entry : allPagesParseResult.entrySet()) {
            List<String> infoList = new ArrayList<>(entry.getValue());
            infoList.add(0, entry.getKey());
            writer.writeNext(infoList.toArray(String[]::new));
            System.out.printf("Wrote %s row%n", i);
            i++;
        }
    }
}

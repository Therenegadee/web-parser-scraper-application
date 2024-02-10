package parser.app.webscraper.scraperlogic;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.app.webscraper.config.ParserConfiguration;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.element.ParseElement;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFile;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;
import parser.app.webscraper.scraperlogic.logic.services.PageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParserRunner {
    @Autowired
    private final ParserConfiguration configuration;
    @Autowired
    private final ChromeOptions chromeOptions;
    private final PageService pageService;
    private final List<ParseElement> parsingTypes = new ArrayList<>();
    private final HashMap<String, List<String>> allPagesParseResult = new HashMap<>();

    @Observed
    public String runParser(ParsingPreset parsingPreset) {
        WebDriver driver = new ChromeDriver(chromeOptions);
        String firstPageURL = parsingPreset.getFirstPageUrl(); // https://zhongchou.modian.com/all/top_comment/all/1
        driver.get(firstPageURL);

        for (ElementLocator e : parsingPreset.getElementLocators()) {
            parsingTypes.add(configuration.parseElement(e, driver));
        }

        List<String> linksToPagesForParse = pageService.getPagesToParseLinks(driver, parsingPreset);

        int parsePageNumber = 1;
        for (String link : linksToPagesForParse) {
            System.out.printf("Парсим информацию со сыылки № %d", parsePageNumber);
            System.out.println();
            driver.get(link);
            List<String> pageParseResult = new ArrayList<>();
            for (ParseElement parseElement : parsingTypes) {
                pageParseResult.add(parseElement.parseByParameters(link));
            }
            allPagesParseResult.put(link, pageParseResult);
            parsePageNumber++;
        }
        System.out.println("Парсинг закончен.");
        driver.quit();

        //todo: фиксануть этот выбор типа файла
        OutputFileType fileType = parsingPreset.getParsingHistory().get(0).getOutputFileType();
        StringBuilder fileNameBuilder = new StringBuilder(UUID.randomUUID().toString());
        fileNameBuilder.append("file");

        if(fileType == OutputFileType.CSV) {
            fileNameBuilder.append(".csv");
        }

        List<String> header = new ArrayList<>();
        header.add(0, "URL");
        header.addAll(parsingPreset.getHeader());

        String fileName = fileNameBuilder.toString();
        String outPutFilePath = "src/main/resources/savedFilesDirectory/" + parsingPreset.getStorageId() + "/" +fileName;
        OutputFile outputFile = new OutputFile(fileType);
        outputFile.exportData(header, allPagesParseResult, outPutFilePath);
        return outPutFilePath;
    }

}

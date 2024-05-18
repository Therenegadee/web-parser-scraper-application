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
import parser.app.webscraper.models.ParserResult;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elementParser.ElementParser;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.FileSaveService;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.ParserPageService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ParserRunner {
    @Autowired
    private final ParserConfiguration configuration;
    @Autowired
    private final ChromeOptions chromeOptions;
    private final ParserPageService parserPageService;
    private final FileSaveService fileSaveService;
    private final List<ElementParser> elementParsers = new ArrayList<>();
    private final HashMap<String, List<String>> allPagesParseResult = new HashMap<>();

    @Observed
    public String runParser(ParsingPreset parsingPreset, ParserResult parserResult) {
        WebDriver driver = new ChromeDriver(chromeOptions);
        String firstPageURL = parsingPreset.getFirstPageUrl(); // https://zhongchou.modian.com/all/top_comment/all/1
        driver.get(firstPageURL);

        for (ElementLocator e : parsingPreset.getElementLocators()) {
            elementParsers.add(configuration.parseElement(e, driver));
        }

        List<String> urls = parserPageService.getPagesToParseLinks(driver, parsingPreset);
        parserPageService.collectDataFromPages(driver, urls, elementParsers, allPagesParseResult);

        OutputFileType fileType = parserResult.getOutputFileType();
        String outPutFilePath = fileSaveService.saveFile(fileType, parsingPreset.getHeader(), allPagesParseResult);
        return outPutFilePath;
    }

}

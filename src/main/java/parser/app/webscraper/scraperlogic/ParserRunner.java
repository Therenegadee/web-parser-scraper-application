package parser.app.webscraper.scraperlogic;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.app.webscraper.config.ParserConfiguration;
import parser.app.webscraper.dto.ParsingPreset;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.element.ParseElement;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFile;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;

import java.time.Duration;
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
    private WebDriver driver;
    private final List<ParseElement> parsingTypes = new ArrayList<>();
    private final HashMap<String, List<String>> allPagesParseResult = new HashMap<>();

    @Observed
    public String runParser(ParsingPreset parsingPreset) {
        driver = new ChromeDriver(chromeOptions);
        String firstPageURL = parsingPreset.getFirstPageUrl(); // https://zhongchou.modian.com/all/top_comment/all/1
        driver.get(firstPageURL);

        for (ElementLocator e : parsingPreset.getElementLocators()) {
            parsingTypes.add(configuration.parseElement(e, driver));
        }

        List<String> linksToPagesForParse = getPagesToParseLinks(driver, parsingPreset);

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

        OutputFileType fileType = parsingPreset.getOutputFileType();
        StringBuilder fileNameBuilder = new StringBuilder(UUID.randomUUID().toString());
        fileNameBuilder.append("file");

        if(fileType == OutputFileType.CSV) {
            fileNameBuilder.append(".csv");
        }

        List<String> header = parsingPreset.getHeader();
        header.add(0, "URL");

        String fileName = fileNameBuilder.toString();
        String outPutFilePath = "src/main/resources/savedFilesDirectory/" + parsingPreset.getUsername() + "/" +fileName;
        OutputFile outputFile = new OutputFile(fileType);
        outputFile.exportData(header, allPagesParseResult, outPutFilePath);
        return outPutFilePath;
    }

    @Observed
    public void clickNextPageButton (String cssSelectorNextPage) {
        WebElement nextPageButton = driver.findElement(By.cssSelector(cssSelectorNextPage)); // "body > div > div.pro_field > div > div > a.next"
        nextPageButton.click();
    }
    @Observed
    public List<String> getPagesToParseLinks(WebDriver driver, ParsingPreset parsingPreset) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        int numOfPagesToParse = parsingPreset.getNumOfPagesToParse();
        List<String> linksToPagesForParse = new ArrayList<>();
        String className = parsingPreset.getClassName(); // pc_ga_pro_index_17
        String tagName = parsingPreset.getTagName(); // a
        if (numOfPagesToParse <= 0) {
            System.err.println("Неверный ввод. Введите число в диапазоне от 1 до n");
        } else if (numOfPagesToParse == 1) {
            System.out.printf("Собираем ссылки со страницы %d...", 1);
            List<WebElement> webElementList = driver.findElements(By.className(className));
            for (WebElement element : webElementList) {
                try {
                    WebElement linkElement = element.findElement(By.tagName(tagName));
                    String href = linkElement.getAttribute("href");
                    if (href != null && !href.isEmpty()) {
                        linksToPagesForParse.add(href);
                    }
                } catch (StaleElementReferenceException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } else if (numOfPagesToParse > 1) {
            System.out.println("Введите CSS Selector путь кнопки переключения следующей страницы: ");
            String cssSelectorNextPage = parsingPreset.getCssSelectorNextPage(); // body > div > div.pro_field > div > div > a.next
            for (int i = 1; i <= numOfPagesToParse; i++) {
                System.out.printf("Собираем ссылки со страницы %d...", i);
                System.out.println();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(className)));
                List<WebElement> webElementList = driver.findElements(By.className(className));
                for (WebElement element : webElementList) {
                    try {
                        WebElement linkElement = element.findElement(By.tagName(tagName));
                        String href = linkElement.getAttribute("href");
                        if (href != null && !href.isEmpty()) {
                            linksToPagesForParse.add(href);
                        }
                    } catch (StaleElementReferenceException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                webElementList.clear();
                clickNextPageButton(cssSelectorNextPage);
            }
        }
        System.out.println("Собрали все ссылки.");
        System.out.println(linksToPagesForParse);
        return linksToPagesForParse;
    }
}

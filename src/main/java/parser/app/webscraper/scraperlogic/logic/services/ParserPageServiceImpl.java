package parser.app.webscraper.scraperlogic.logic.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elementParser.ElementParser;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.PaginationService;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.ParserPageService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ParserPageServiceImpl implements ParserPageService {
    private final PaginationService paginationService;

    //todo: добавить обработку ошибок и логирование
    @Observed
    @Override
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
                paginationService.clickNextPageButton(driver, cssSelectorNextPage);
            }
        }
        System.out.println("Собрали все ссылки.");
        System.out.println(linksToPagesForParse);
        return linksToPagesForParse;
    }

    @Observed
    @Override
    public void collectDataFromPages(WebDriver driver, List<String> urls, List<ElementParser> elementParsers, Map<String, List<String>> allPagesParseResult) {
        int parsePageNumber = 1;
        for (String link : urls) {
            System.out.printf("Парсим информацию со сыылки № %d", parsePageNumber);
            System.out.println();
            driver.get(link);
            List<String> pageParseResult = new ArrayList<>();
            for (ElementParser elementParser : elementParsers) {
                pageParseResult.add(elementParser.parseByParameters());
            }
            allPagesParseResult.put(link, pageParseResult);
            parsePageNumber++;
        }
        System.out.println("Парсинг закончен.");
        driver.quit();
    }
}

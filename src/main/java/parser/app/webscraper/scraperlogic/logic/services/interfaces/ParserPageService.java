package parser.app.webscraper.scraperlogic.logic.services.interfaces;

import org.openqa.selenium.WebDriver;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elementParser.ElementParser;

import java.util.List;
import java.util.Map;

public interface ParserPageService {

    List<String> getPagesToParseLinks(WebDriver driver, ParsingPreset parsingPreset);

    void collectDataFromPages(WebDriver driver, List<String> urls, List<ElementParser> elementParsers, Map<String, List<String>> allPagesParseResult);
}

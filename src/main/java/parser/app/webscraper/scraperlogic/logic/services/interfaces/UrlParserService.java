package parser.app.webscraper.scraperlogic.logic.services.interfaces;

import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elements.ElementParser;

import java.util.List;

public interface UrlParserService {

    List<String> getPagesToParseLinks(ParsingPreset parsingPreset);

    List<String> collectDataFromPage(String url, List<ElementParser> elementParsers);
}

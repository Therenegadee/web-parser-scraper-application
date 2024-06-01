package parser.app.webscraper.scraperlogic.logic.services.interfaces;

import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elementParser.ElementParser;

import java.util.List;
import java.util.Map;

public interface ParserPageService {

    List<String> getPagesToParseLinks(ParsingPreset parsingPreset);

    void collectDataFromPages(List<String> urls, List<ElementParser> elementParsers, Map<String, List<String>> allPagesParseResult);
}

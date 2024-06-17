package parser.app.webscraper.scraperlogic.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elements.ElementParser;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.UrlParserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SyncParserExecutor implements ParserExecutor {

    private final UrlParserService urlParserService;

    @Override
    public Map<String, List<String>> execute(ParsingPreset parsingPreset) {
        List<ElementParser> parseElements = parsingPreset.getElementLocators().stream()
                .map(ElementParser::new)
                .toList();
        List<String> urls = urlParserService.getPagesToParseLinks(parsingPreset);
        Map<String, List<String>> parsingResult = new HashMap<>();
        for (String url : urls) {
            parsingResult.put(url, urlParserService.collectDataFromPage(url, parseElements));
        }
        return parsingResult;
    }
}

package parser.app.webscraper.scraperlogic.task;

import lombok.RequiredArgsConstructor;
import parser.app.webscraper.scraperlogic.logic.elements.ElementParser;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.UrlParserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class UrlParsingTask implements Callable<Map<String, List<String>>> {

    private final BlockingQueue<String> queue;
    private final UrlParserService urlParserService;
    private final List<ElementParser> parseElements;

    @Override
    public Map<String, List<String>> call() {
        Map<String, List<String>> result = new HashMap<>();
        while (!queue.isEmpty()) {
            String url = queue.poll();
            if (url != null) {
               result.put(url, urlParserService.collectDataFromPage(url, parseElements));
            }
        }
        return result;
    }

}

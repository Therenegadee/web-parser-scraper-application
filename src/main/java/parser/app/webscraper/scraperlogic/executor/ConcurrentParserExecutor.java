package parser.app.webscraper.scraperlogic.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elements.ElementParser;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.UrlParserService;
import parser.app.webscraper.scraperlogic.task.UrlParsingTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ConcurrentParserExecutor implements ParserExecutor {

    private final UrlParserService urlParserService;

    @Override
    public Map<String, List<String>> execute(ParsingPreset parsingPreset) throws ExecutionException, InterruptedException {
        List<ElementParser> parseElements = parsingPreset.getElementLocators().stream()
                .map(ElementParser::new)
                .toList();
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(urlParserService.getPagesToParseLinks(parsingPreset));
        int numThreads = getAvailableProcessorsNumber();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        List<Future<Map<String, List<String>>>> futureResults = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            futureResults.add(executorService.submit(new UrlParsingTask(queue, urlParserService, parseElements)));
        }

        Map<String, List<String>> parsingResults = new HashMap<>();
        for (Future<Map<String, List<String>>> future : futureResults) {
            parsingResults.putAll(future.get());
        }

        executorService.shutdown();
        return parsingResults;
    }

    private int getAvailableProcessorsNumber() {
        return Runtime.getRuntime().availableProcessors();
    }
}

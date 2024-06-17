package parser.app.webscraper.scraperlogic.executor;

import parser.app.webscraper.models.ParsingPreset;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface ParserExecutor {

    Map<String, List<String>> execute(ParsingPreset parsingPreset) throws ExecutionException, InterruptedException;

}

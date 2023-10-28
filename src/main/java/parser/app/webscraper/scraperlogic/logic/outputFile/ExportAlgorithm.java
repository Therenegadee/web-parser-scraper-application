package parser.app.webscraper.scraperlogic.logic.outputFile;

import java.util.HashMap;
import java.util.List;

public interface ExportAlgorithm {
    void exportData(List<String> header, HashMap<String, List<String>> allPagesParseResult, String pathToOutput);
}

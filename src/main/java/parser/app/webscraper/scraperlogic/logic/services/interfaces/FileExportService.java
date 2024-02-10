package parser.app.webscraper.scraperlogic.logic.services.interfaces;

import java.util.HashMap;
import java.util.List;

public interface FileExportService {
    void exportData(List<String> header, HashMap<String, List<String>> allPagesParseResult, String pathToOutput);
}

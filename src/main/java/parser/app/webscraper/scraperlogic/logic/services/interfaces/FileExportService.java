package parser.app.webscraper.scraperlogic.logic.services.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FileExportService {
    void exportData(List<String> header, Map<String, List<String>> allPagesParseResult, String pathToOutput);
}

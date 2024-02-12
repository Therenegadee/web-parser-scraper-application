package parser.app.webscraper.scraperlogic.logic.services.interfaces;

import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;

import java.util.List;
import java.util.Map;

public interface FileSaveService {
    String saveFile(OutputFileType fileType, List<String> presetHeader, Map<String, List<String>> allPagesParseResult);
}

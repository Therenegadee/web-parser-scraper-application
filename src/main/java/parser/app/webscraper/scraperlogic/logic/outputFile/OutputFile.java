package parser.app.webscraper.scraperlogic.logic.outputFile;

import lombok.RequiredArgsConstructor;
import parser.app.webscraper.scraperlogic.logic.services.CsvFileExportService;
import parser.app.webscraper.scraperlogic.logic.services.ExcelFileExportService;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.FileExportService;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class OutputFile {
    private FileExportService fileExportService;

    public OutputFile(OutputFileType type) {
        this.fileExportService = switch (type){
            case XLSX -> new ExcelFileExportService();
            case CSV -> new CsvFileExportService();
        };
    }

    public void exportData(List<String> header, Map<String, List<String>> allPagesParseResult, String pathToOutput) {
        fileExportService.exportData(header, allPagesParseResult, pathToOutput);
    }
}

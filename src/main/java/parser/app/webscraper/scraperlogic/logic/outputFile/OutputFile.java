package parser.app.webscraper.scraperlogic.logic.outputFile;

import lombok.RequiredArgsConstructor;
import parser.app.webscraper.scraperlogic.logic.services.CsvFileExportService;
import parser.app.webscraper.scraperlogic.logic.services.ExcelFileExportService;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.FileExportService;

import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
public class OutputFile {
    private FileExportService fileExportService;
    private OutputFileType type;

    public OutputFile(OutputFileType type) {
        this.type=type;
        this.fileExportService = switch (type){
            case XLSX -> new ExcelFileExportService();
            case CSV -> new CsvFileExportService();
        };
    }

    public void exportData(List<String> header, HashMap<String, List<String>> allPagesParseResult, String pathToOutput) {
        fileExportService.exportData(header, allPagesParseResult, pathToOutput);
    }
}

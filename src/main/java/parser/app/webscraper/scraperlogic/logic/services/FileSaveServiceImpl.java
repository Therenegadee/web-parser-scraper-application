package parser.app.webscraper.scraperlogic.logic.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFile;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.FileSaveService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileSaveServiceImpl implements FileSaveService {

    @Observed
    @Override
    public String saveFile(OutputFileType fileType, List<String> presetHeader, Map<String, List<String>> allPagesParseResult) {
        StringBuilder fileNameBuilder = new StringBuilder(UUID.randomUUID().toString());
        fileNameBuilder.append("file");

        if(fileType == OutputFileType.CSV) {
            fileNameBuilder.append(".csv");
        }

        List<String> header = new ArrayList<>();
        header.add(0, "URL");
        header.addAll(presetHeader);

        String fileName = fileNameBuilder.toString();
        String outPutFilePath = "src/main/resources/savedFilesDirectory/" +fileName;
        OutputFile outputFile = new OutputFile(fileType);
        outputFile.exportData(header, allPagesParseResult, outPutFilePath);
        return outPutFilePath;
    }
}

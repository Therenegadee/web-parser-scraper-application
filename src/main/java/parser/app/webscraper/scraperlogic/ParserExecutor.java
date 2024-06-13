package parser.app.webscraper.scraperlogic;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.models.ParsingResult;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elements.ElementParser;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.FileSaveService;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.ParserPageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParserExecutor {

    private final ParserPageService parserPageService;
    private final FileSaveService fileSaveService;
    private final List<ElementParser> elementParsers = new ArrayList<>();
    private final HashMap<String, List<String>> allPagesParseResult = new HashMap<>();

    @Observed
    public String runParser(ParsingPreset parsingPreset, ParsingResult parsingResult) {
        for (ElementLocator e : parsingPreset.getElementLocators()) {
            elementParsers.add(new ElementParser(e));
        }

        List<String> urls = parserPageService.getPagesToParseLinks(parsingPreset);
        parserPageService.collectDataFromPages(urls, elementParsers, allPagesParseResult);

        OutputFileType fileType = parsingResult.getOutputFileType();
        String outPutFilePath = fileSaveService.saveFile(fileType, parsingPreset.getHeader(), allPagesParseResult);
        return outPutFilePath;
    }

}

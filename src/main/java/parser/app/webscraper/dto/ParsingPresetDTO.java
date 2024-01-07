package parser.app.webscraper.dto;

import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;

import java.util.Date;
import java.util.List;

public class ParsingPresetDTO {
    private String firstPageUrl;
    private int numOfPagesToParse;
    private String className;
    private String tagName;
    private String cssSelectorNextPage;
    private List<String> header;
    private List<ElementLocator> elementLocators;
    private Date date;
    private OutputFileType outputFileType;
}

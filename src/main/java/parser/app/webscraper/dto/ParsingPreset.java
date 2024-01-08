package parser.app.webscraper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;

import java.util.Date;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParsingPreset {
    private String username;
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

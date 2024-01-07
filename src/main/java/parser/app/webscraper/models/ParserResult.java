package parser.app.webscraper.models;

import lombok.*;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParserResult {
    private Long id;
    private Date date;
    private String linkToDownloadResults;
    private OutputFileType outputFileType;
    private UserParserSetting userParserSetting;
}

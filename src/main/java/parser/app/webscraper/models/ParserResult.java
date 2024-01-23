package parser.app.webscraper.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ParserResult {
    @Id
    private Long id;
    private Date date;
    private String linkToDownloadResults;
    private OutputFileType outputFileType;
    private UUID userParserSettingId;
}

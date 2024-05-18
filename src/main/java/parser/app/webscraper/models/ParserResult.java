package parser.app.webscraper.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;
import parser.app.webscraper.scraperlogic.logic.outputFile.OutputFileType;

import java.util.Date;

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
    private ObjectId presetId;
}

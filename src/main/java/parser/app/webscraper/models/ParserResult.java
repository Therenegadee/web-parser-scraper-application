package parser.app.webscraper.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParserResult {
    private Long id;
    private UserParserSetting userParserSettings;
    private Long userId;
    private String linkToDownloadResults;
}

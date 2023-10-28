package parser.app.webscraper.scraperlogic.logic.parameter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OneParseParameter implements ParseParameter {
    private final String parameter;
}

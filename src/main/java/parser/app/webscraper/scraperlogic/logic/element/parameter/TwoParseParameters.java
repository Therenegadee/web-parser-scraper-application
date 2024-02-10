package parser.app.webscraper.scraperlogic.logic.element.parameter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public record TwoParseParameters(String parameter1, String parameter2) implements ParseParameter {
}

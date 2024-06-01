package parser.app.webscraper.scraperlogic.logic.elementParser;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.InternalParseException;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elementParser.interfaces.ParseAlgorithm;

import java.util.Objects;

@Getter
@Setter
public class ElementParser {

    private final ParseAlgorithm parseAlgorithm;
    private final ElementLocator parseElementDetails;

    @Autowired
    public ElementParser(ElementLocator elementLocator) {
        this.parseElementDetails = elementLocator;
        this.parseAlgorithm = switch (elementLocator.getType()) {
            case CSS -> new CssSelectorElementParser();
            case TAG_ATTR -> new TagAttrElementParser();
            case ID -> new IdElementParser();
            case CLASS_NAME -> new ClassnameElementParser();
        };
    }

    public String parseByParameters(Document page) {
        return parseAlgorithm.parseByParameters(page, parseElementDetails);
    }
}

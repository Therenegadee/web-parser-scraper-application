package parser.app.webscraper.scraperlogic.logic.elements;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elements.interfaces.ParseableElement;


@Getter
@Setter
public class ElementParser {

    private final ParseableElement parseableElement;
    private final ElementLocator parseElementDetails;

    @Autowired
    public ElementParser(ElementLocator elementLocator) {
        this.parseElementDetails = elementLocator;
        this.parseableElement = switch (elementLocator.getType()) {
            case CSS -> new CssSelectorElement();
            case TAG_ATTR -> new TagAttrElement();
            case ID -> new IdElement();
            case CLASS_NAME -> new ClassNameElement();
        };
    }

    public String parseByParameters(Document page) {
        return parseableElement.parseByParameters(page, parseElementDetails);
    }
}

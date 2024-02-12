package parser.app.webscraper.scraperlogic.logic.elementParser;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elementParser.interfaces.ParseAlgorithm;

@Getter
@Setter
public class ElementParser {

    private final ParseAlgorithm parseAlgorithm;
    private final ElementLocator parseElementDetails;

    @Autowired
    public ElementParser(WebDriver driver, ElementLocator elementLocator) {
        this.parseElementDetails = elementLocator;
        this.parseAlgorithm = switch (elementLocator.getType()) {
            case XPATH -> new XPathElementParser(driver);
            case CSS -> new CssSelectorElementParser(driver);
            case TAG_ATTR -> new TagAttrElementParser(driver);
            case ID -> new IdElementParser(driver);
            case CLASS_NAME -> new ClassnameElementParser(driver);
        };
    }

    public String parseByParameters() {
        return parseAlgorithm.parseByParameters(parseElementDetails);
    }
}

package parser.app.webscraper.scraperlogic.logic.element;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.enums.ElementType;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseAlgorithm;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseParameter;

import java.util.List;
public class ParseElement {

    private final ParseAlgorithm parseAlgorithm;
    private final ElementType type;
    private final List<ParseParameter> parameters;

    @Autowired
    public ParseElement(WebDriver driver, ElementType type, List<ParseParameter> parameters) {
        this.type = type;
        this.parameters = parameters;
        this.parseAlgorithm = switch (this.type) {
            case XPATH -> new XPathElement(driver);
            case CSS -> new CssSelectorElement(driver);
            case TAG_ATTR -> new TagAttrElement(driver);
        };
    }

    public String parseByParameters(String url) {
        return parameters
                .stream()
                .map(parameter -> parseAlgorithm.parseByParameters(parameter, url))
                .findFirst()
                .get();
    }
}

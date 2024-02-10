package parser.app.webscraper.scraperlogic.logic.element;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.enums.ElementType;
import parser.app.webscraper.scraperlogic.logic.element.interfaces.ParseAlgorithm;
import parser.app.webscraper.scraperlogic.logic.element.parameter.ParseParameter;

@Component
@Getter
@Setter
public class ParseElement {

    private final ParseAlgorithm parseAlgorithm;
    private final ElementType type;
    private final ParseParameter parameters;

    @Autowired
    public ParseElement(WebDriver driver, ElementType type, ParseParameter parameters) {
        this.type = type;
        this.parameters = parameters;
        this.parseAlgorithm = switch (this.type) {
            case XPATH -> new XPathElement(driver);
            case CSS -> new CssSelectorElement(driver);
            case TAG_ATTR -> new TagAttrElement(driver);
            case COUNTABLE -> new CountableElement(driver);
        };
    }

    public String parseByParameters(String url) {
        return parseAlgorithm.parseByParameters(parameters, url);
    }

    public WebElement parseByParametersWithWebElementInfo(String url) {
        return parseAlgorithm.parseByParametersWithWebElementInfo(parameters, url);
    }
}

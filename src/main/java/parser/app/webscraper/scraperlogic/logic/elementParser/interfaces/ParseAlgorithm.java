package parser.app.webscraper.scraperlogic.logic.elementParser.interfaces;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import parser.app.webscraper.models.ElementLocator;

import java.util.List;

public interface ParseAlgorithm {
    String parseByParameters(ElementLocator parseElementDetails);
    WebElement parseByParametersWithWebElementInfo(ElementLocator parseElementDetails);

    List<WebElement> parseByParametersWithAllWebElementsInfo(ElementLocator parseElementDetails);

    default List<WebElement> getCountableElements(WebElement webElement, ElementLocator parseElementDetails) {
        return switch (parseElementDetails.getCountableElementType()) {
            case CSS -> webElement.findElements(By.cssSelector(parseElementDetails.getPathToCountableLocator()));
            case XPATH -> webElement.findElements(By.xpath(parseElementDetails.getPathToCountableLocator()));
            case TAG_ATTR -> webElement.findElements(By.tagName(parseElementDetails.getPathToCountableLocator()));
            case ID -> webElement.findElements(By.id(parseElementDetails.getPathToCountableLocator()));
            case CLASS_NAME -> webElement.findElements(By.className(parseElementDetails.getPathToCountableLocator()));
        };
    }
}

package parser.app.webscraper.scraperlogic.logic.elementParser.interfaces;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.app.webscraper.models.ElementLocator;

public interface ParseAlgorithm {
    String parseByParameters(Document page, ElementLocator parseElementDetails);
    Element parseByParametersWithWebElementInfo(Document page, ElementLocator parseElementDetails);

    Elements parseByParametersWithAllWebElementsInfo(Document page, ElementLocator parseElementDetails);

    //todo : bad contract with id, there couldn't be several elements with id
    default Elements getCountableElements(Element webElement, ElementLocator parseElementDetails) {
        return switch (parseElementDetails.getCountableElementType()) {
            case CSS -> webElement.select(parseElementDetails.getPathToCountableLocator());
            case TAG_ATTR -> webElement.getElementsByTag(parseElementDetails.getPathToCountableLocator());
            case ID -> new Elements(webElement.getElementById(parseElementDetails.getPathToCountableLocator()));
            case CLASS_NAME -> webElement.getElementsByClass(parseElementDetails.getPathToCountableLocator());
        };
    }
}

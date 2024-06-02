package parser.app.webscraper.scraperlogic.logic.elements.interfaces;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.app.webscraper.models.ElementLocator;

public interface ParseableElement {
    String parseByParameters(Document page, ElementLocator parseElementDetails);
    Element parseByParametersWithWebElementInfo(Document page, ElementLocator parseElementDetails);

    Elements parseByParametersWithAllWebElementsInfo(Document page, ElementLocator parseElementDetails);
}

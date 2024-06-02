package parser.app.webscraper.scraperlogic.logic.elements.interfaces;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.app.webscraper.models.ElementLocator;

public interface CountableElement {

    Elements getCountableElements(Element webElement, ElementLocator parseElementDetails);

}

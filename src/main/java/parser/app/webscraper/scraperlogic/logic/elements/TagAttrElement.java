package parser.app.webscraper.scraperlogic.logic.elements;

import lombok.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elements.interfaces.CountableElement;
import parser.app.webscraper.scraperlogic.logic.elements.interfaces.ParseableElement;

@Getter
@Setter
public class TagAttrElement implements ParseableElement, CountableElement {

    @Autowired
    public TagAttrElement() {

    }

    @Override
    public String parseByParameters(Document page, ElementLocator parseElementDetails) {
        Element webElement = parseByParametersWithWebElementInfo(page, parseElementDetails);
        if (parseElementDetails.isCountable()) {
            return String.valueOf(getCountableElements(webElement, parseElementDetails).size());
        }
        return webElement.getElementsByAttribute(parseElementDetails.getExtraPointer()).first().text();
    }


    @Override
    public Element parseByParametersWithWebElementInfo(Document page, ElementLocator parseElementDetails) {
        return page.getElementsByTag(parseElementDetails.getPathToLocator()).first();
    }

    @Override
    public Elements parseByParametersWithAllWebElementsInfo(Document page, ElementLocator parseElementDetails) {
        return page.getElementsByTag(parseElementDetails.getPathToLocator());
    }

    @Override
    public Elements getCountableElements(Element webElement, ElementLocator parseElementDetails) {
        return webElement.getElementsByTag(parseElementDetails.getPathToCountableLocator());
    }
}

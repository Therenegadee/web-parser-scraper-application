package parser.app.webscraper.scraperlogic.logic.elementParser;

import lombok.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elementParser.interfaces.ParseAlgorithm;

@Getter
@Setter
public class TagAttrElementParser implements ParseAlgorithm {

    @Autowired
    public TagAttrElementParser() {

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
}

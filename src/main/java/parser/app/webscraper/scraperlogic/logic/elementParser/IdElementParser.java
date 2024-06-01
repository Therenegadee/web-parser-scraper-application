package parser.app.webscraper.scraperlogic.logic.elementParser;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elementParser.interfaces.ParseAlgorithm;

import java.util.Collections;

@Getter
@Setter
public class IdElementParser implements ParseAlgorithm {

    @Autowired
    public IdElementParser() {

    }

    @Override
    public String parseByParameters(Document page, ElementLocator parseElementDetails) {
        Element webElement = parseByParametersWithWebElementInfo(page, parseElementDetails);
        if (parseElementDetails.isCountable()) {
            return String.valueOf(getCountableElements(webElement, parseElementDetails).size());
        }
        return webElement.text();
    }

    @Override
    public Element parseByParametersWithWebElementInfo(Document page, ElementLocator parseElementDetails) {
        return page.getElementById(parseElementDetails.getPathToLocator());
    }

    // TODO: плохой контракт, фиксануть
    @Override
    public Elements parseByParametersWithAllWebElementsInfo(Document page, ElementLocator parseElementDetails) {
        return new Elements(Collections.singletonList(page.getElementById(parseElementDetails.getPathToLocator())));
    }
}

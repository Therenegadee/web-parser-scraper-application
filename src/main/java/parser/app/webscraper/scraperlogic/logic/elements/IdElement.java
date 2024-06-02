package parser.app.webscraper.scraperlogic.logic.elements;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elements.interfaces.ParseableElement;

import java.util.Collections;

@Getter
@Setter
public class IdElement implements ParseableElement {

    @Autowired
    public IdElement() {

    }

    @Override
    public String parseByParameters(Document page, ElementLocator parseElementDetails) {
        return parseByParametersWithWebElementInfo(page, parseElementDetails).text();
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

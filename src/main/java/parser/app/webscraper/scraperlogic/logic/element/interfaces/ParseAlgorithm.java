package parser.app.webscraper.scraperlogic.logic.element.interfaces;

import org.openqa.selenium.WebElement;
import parser.app.webscraper.scraperlogic.logic.element.parameter.ParseParameter;

public interface ParseAlgorithm {
    String parseByParameters(ParseParameter abstractParseParameter, String url);
    WebElement parseByParametersWithWebElementInfo(ParseParameter abstractParseParameter, String url);
}

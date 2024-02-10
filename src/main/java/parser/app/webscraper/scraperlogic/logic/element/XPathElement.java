package parser.app.webscraper.scraperlogic.logic.element;

import lombok.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.element.interfaces.ParseAlgorithm;
import parser.app.webscraper.scraperlogic.logic.element.parameter.OneParseParameter;
import parser.app.webscraper.scraperlogic.logic.element.parameter.ParseParameter;

import java.time.Duration;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class XPathElement
        extends ElementLocator
        implements ParseAlgorithm {
    private final WebDriver driver;

    @Override
    public String parseByParameters(ParseParameter abstractParseParameter, String url) {
        return parseByParametersWithWebElementInfo(abstractParseParameter, url).getText();
    }

    @Override
    public WebElement parseByParametersWithWebElementInfo(ParseParameter abstractParseParameter, String url) {
        String xPath = ((OneParseParameter)abstractParseParameter).parameter();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
        return driver.findElement(By.xpath(xPath));
    }
}

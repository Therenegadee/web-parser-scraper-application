package parser.app.webscraper.scraperlogic.logic.element;

import lombok.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.element.interfaces.ParseAlgorithm;
import parser.app.webscraper.scraperlogic.logic.element.parameter.ParseParameter;
import parser.app.webscraper.scraperlogic.logic.element.parameter.TwoParseParameters;

import java.time.Duration;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class TagAttrElement
        extends ElementLocator
        implements ParseAlgorithm {

    private final WebDriver driver;

    //todo: избавиться от интефейса ParseParameter и его рекордов
    @Override
    public String parseByParameters(ParseParameter abstractParseParameter, String url) {
        TwoParseParameters twoParseParameters = (TwoParseParameters) abstractParseParameter;
        return parseByParametersWithWebElementInfo(abstractParseParameter, url)
                .getAttribute(twoParseParameters.getParameter2());
    }

    @Override
    public WebElement parseByParametersWithWebElementInfo(ParseParameter abstractParseParameter, String url) {
        TwoParseParameters twoParseParameters = (TwoParseParameters) abstractParseParameter;
        String tagName = twoParseParameters.getParameter1();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(tagName)));
        return driver.findElement(By.tagName(tagName));
    }
}

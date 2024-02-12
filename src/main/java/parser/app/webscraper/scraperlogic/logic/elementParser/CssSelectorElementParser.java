package parser.app.webscraper.scraperlogic.logic.elementParser;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elementParser.interfaces.ParseAlgorithm;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
public class CssSelectorElementParser implements ParseAlgorithm {

    private final WebDriver driver;

    @Autowired
    public CssSelectorElementParser(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public String parseByParameters(ElementLocator parseElementDetails) {
        WebElement webElement = parseByParametersWithWebElementInfo(parseElementDetails);
        if (parseElementDetails.isCountable()) {
            return String.valueOf(getCountableElements(webElement, parseElementDetails).size());
        }
        return webElement.getText();
    }

    @Override
    public WebElement parseByParametersWithWebElementInfo(ElementLocator parseElementDetails) {
        String cssSelector = parseElementDetails.getPathToLocator();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        return driver.findElement(By.cssSelector(cssSelector));
    }

    @Override
    public List<WebElement> parseByParametersWithAllWebElementsInfo(ElementLocator parseElementDetails) {
        String cssSelector = parseElementDetails.getPathToLocator();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        return driver.findElements(By.cssSelector(cssSelector));
    }

}

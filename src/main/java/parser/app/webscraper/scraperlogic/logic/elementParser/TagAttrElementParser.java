package parser.app.webscraper.scraperlogic.logic.elementParser;

import lombok.*;
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
public class TagAttrElementParser implements ParseAlgorithm {

    private final WebDriver driver;

    @Autowired
    public TagAttrElementParser(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public String parseByParameters(ElementLocator parseElementDetails) {
        WebElement webElement = parseByParametersWithWebElementInfo(parseElementDetails);
        if (parseElementDetails.isCountable()) {
            return String.valueOf(getCountableElements(webElement, parseElementDetails).size());
        }
        return webElement.getAttribute(parseElementDetails.getExtraPointer());
    }


    @Override
    public WebElement parseByParametersWithWebElementInfo(ElementLocator parseElementDetails) {
        String tagName = parseElementDetails.getPathToLocator();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(tagName)));
        return driver.findElement(By.tagName(tagName));
    }

    @Override
    public List<WebElement> parseByParametersWithAllWebElementsInfo(ElementLocator parseElementDetails) {
        String tagName = parseElementDetails.getPathToLocator();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(tagName)));
        return driver.findElements(By.tagName(tagName));
    }
}

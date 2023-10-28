package parser.app.webscraper.scraperlogic.logic.element;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.parameter.OneParseParameter;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseAlgorithm;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseParameter;

import java.time.Duration;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class XPathElement extends ElementLocator implements ParseAlgorithm {
    @Autowired
    private WebDriver driver;

    @Autowired
    public XPathElement(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public String parseByParameters(ParseParameter abstractParseParameter, String url) {
        String xPath = ((OneParseParameter)abstractParseParameter).getParameter();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement element = driver.findElement(By.xpath(xPath));
        String elementValue = element.getText();
        return elementValue;
    }

}

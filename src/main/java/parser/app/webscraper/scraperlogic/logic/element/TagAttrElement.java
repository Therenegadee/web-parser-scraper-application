package parser.app.webscraper.scraperlogic.logic.element;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseAlgorithm;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseParameter;
import parser.app.webscraper.scraperlogic.logic.parameter.TwoParseParameters;

import java.time.Duration;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TagAttrElement extends ElementLocator implements ParseAlgorithm {
    @Autowired
    private WebDriver driver;
    private String attributeName;
    public TagAttrElement(WebDriver driver) {
        this.driver=driver;
    }

    @Override
    public String parseByParameters(ParseParameter abstractParseParameter, String url) {
        TwoParseParameters twoParseParameters = (TwoParseParameters) abstractParseParameter;
        String tagName = twoParseParameters.getParameter1();
        String attributeName = twoParseParameters.getParameter2();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(tagName)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement tagElement = driver.findElement(By.tagName(tagName));
        String elementValue = tagElement.getAttribute(attributeName);
        return elementValue;
    }
}

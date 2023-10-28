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
import parser.app.webscraper.scraperlogic.logic.parameter.OneParseParameter;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseAlgorithm;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseParameter;

import java.time.Duration;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CssSelectorElement extends ElementLocator implements ParseAlgorithm {
    @Autowired
    private WebDriver driver;

    @Override
    public String parseByParameters(ParseParameter abstractParseParameter, String url) {
        String cssSelector = ((OneParseParameter)abstractParseParameter).getParameter();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        String elementValue = element.getText();
        return elementValue;
    }
}

package parser.app.webscraper.scraperlogic.logic.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.PaginationService;

@Service
@RequiredArgsConstructor
public class PaginationServiceImpl implements PaginationService {
    @Observed
    @Override
    public void clickNextPageButton (WebDriver driver, String cssSelectorNextPage) {
        WebElement nextPageButton = driver.findElement(By.cssSelector(cssSelectorNextPage)); // "body > div > div.pro_field > div > div > a.next"
        nextPageButton.click();
    }
}

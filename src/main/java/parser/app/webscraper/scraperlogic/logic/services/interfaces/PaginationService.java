package parser.app.webscraper.scraperlogic.logic.services.interfaces;

import org.openqa.selenium.WebDriver;

public interface PaginationService {
    void clickNextPageButton (WebDriver driver, String cssSelectorNextPage);
}

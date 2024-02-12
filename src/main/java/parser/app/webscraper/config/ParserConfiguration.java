package parser.app.webscraper.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.scraperlogic.logic.elementParser.ElementParser;

@Configuration
public class ParserConfiguration {
    @Value("${webdriver.http.factory}")
    private String webDriverHttpFactory;
    @Value("${webdriver.chrome.driver}")
    private String webDriverChromeDriver;

    @Bean
    public ChromeOptions chromeOptions() {
        System.setProperty("webdriver.http.factory", webDriverHttpFactory);
        System.setProperty("webdriver.chrome.driver", webDriverChromeDriver);
        return new ChromeOptions();
    }

    @Bean
    @Scope("prototype")
    public ElementParser parseElement(ElementLocator parseElementDetails, WebDriver driver) {
        return new ElementParser(driver, parseElementDetails);
    }
}

package parser.app.webscraper.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import parser.app.webscraper.exceptions.BadRequestException;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.models.enums.ElementType;
import parser.app.webscraper.scraperlogic.logic.element.ParseElement;
import parser.app.webscraper.scraperlogic.logic.element.TagAttrElement;
import parser.app.webscraper.scraperlogic.logic.element.parameter.OneParseParameter;
import parser.app.webscraper.scraperlogic.logic.element.parameter.ParseParameter;
import parser.app.webscraper.scraperlogic.logic.element.parameter.TwoParseParameters;

import java.util.ArrayList;
import java.util.List;

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
    public ParseElement parseElement(ElementLocator e, WebDriver driver) {
        switch (e.getType()) {
            case CSS -> {
                return new ParseElement(driver, ElementType.CSS, new OneParseParameter(e.getPathToLocator()));
            }
            case XPATH -> {
                return new ParseElement(driver, ElementType.XPATH, new OneParseParameter(e.getPathToLocator()));
            }
            case TAG_ATTR -> {
                return new ParseElement(driver,
                        ElementType.TAG_ATTR,
                        new TwoParseParameters(e.getPathToLocator(), e.getExtraPointer())
                );
            }
//            case COUNTABLE -> {
//                return new ParseElement(driver, ElementType.COUNTABLE, parameterList);
//            }
            default -> throw new BadRequestException(
                    String.format("Incorrect type of element with name %s. Type can't be: %s", e.getName(), e.getType().getValue())
            );
        }
    }
}

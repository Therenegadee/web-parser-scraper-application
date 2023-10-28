package parser.app.webscraper.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.models.enums.ElementType;
import parser.app.webscraper.scraperlogic.logic.element.CssSelectorElement;
import parser.app.webscraper.scraperlogic.logic.element.ParseElement;
import parser.app.webscraper.scraperlogic.logic.element.TagAttrElement;
import parser.app.webscraper.scraperlogic.logic.element.XPathElement;
import parser.app.webscraper.scraperlogic.logic.parameter.OneParseParameter;
import parser.app.webscraper.scraperlogic.logic.parameter.ParseParameter;
import parser.app.webscraper.scraperlogic.logic.parameter.TwoParseParameters;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource(value = "classpath:application.yml")
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
        List<ParseParameter> parameterList = new ArrayList<>();
        if (e instanceof XPathElement) {
            parameterList.add(new OneParseParameter(e.getPathToLocator()));
            return new ParseElement(driver, ElementType.XPATH, parameterList);
        }
        if (e instanceof CssSelectorElement) {
            parameterList.add(new OneParseParameter(e.getPathToLocator()));
            return new ParseElement(driver, ElementType.CSS, parameterList);
        }
        if (e instanceof TagAttrElement) {
            parameterList.add(
                    new TwoParseParameters(e.getPathToLocator(), ((TagAttrElement) e).getAttributeName()));
            return new ParseElement(driver, ElementType.TAG_ATTR, parameterList);
        }
        return null;
    }
}

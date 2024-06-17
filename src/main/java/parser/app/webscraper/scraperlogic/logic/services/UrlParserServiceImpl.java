package parser.app.webscraper.scraperlogic.logic.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.scraperlogic.logic.elements.ElementParser;
import parser.app.webscraper.scraperlogic.logic.services.interfaces.UrlParserService;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Component
public class UrlParserServiceImpl implements UrlParserService {

    //todo: добавить обработку ошибок и логирование
    @Observed
    @Override
    public List<String> getPagesToParseLinks(ParsingPreset parsingPreset) {
        Document page; // https://zhongchou.modian.com/all/top_comment/all/1
        try {
            page = Jsoup.connect(parsingPreset.getFirstPageUrl()).get();
        } catch (IOException e) {
            String msg = "Error occurred while connecting to the first page URL: %s. Document object wasn't initialized.";
            throw new IllegalArgumentException(String.format(msg, parsingPreset.getFirstPageUrl()));
        }
        int numOfPagesToParse = parsingPreset.getNumOfPagesToParse();
        List<String> linksToPagesForParse = new ArrayList<>();
        String className = parsingPreset.getClassName(); // pc_ga_pro_index_17
        String tagName = parsingPreset.getTagName(); // a
        if (numOfPagesToParse <= 0) {
            String msg = "Number of pages to parse couldn't be less or equals to 0. The given value is: %d";
            throw new IllegalArgumentException(String.format(msg, numOfPagesToParse));
        } else if (Objects.nonNull(page)) {
            for (int i = 0; i < numOfPagesToParse; ) {
                System.out.printf("[%d page of %d] Collecting links from web-page URL: %s", i, numOfPagesToParse, page.location());
                Elements elements = page.getElementsByClass(className);
                for (Element element : elements) {
                    Element linkElement = element.getElementsByTag(tagName).first();
                    String href = linkElement.getElementsByAttribute("href").first().text();
                    if (Objects.nonNull(href) && !href.isEmpty()) {
                        linksToPagesForParse.add(href);
                    }
                }
                if (numOfPagesToParse > 1) {
                    try {
                        page = getNextPage(page, parsingPreset.getCssSelectorNextPage()); // body > div > div.pro_field > div > div > a.next
                    } catch (IOException e) {
                        System.err.println("Error occurred while moving to the next page.");
                        // TODO: add to error links
                        //  and try to add the repeatable tries to connect to the next page
                    }
                }
                i++;
            }
        }
        System.out.println("All links were collected.");
        return linksToPagesForParse;
    }

    @Observed
    @Override
    public List<String> collectDataFromPage(String url, List<ElementParser> elementParsers) {
        Document page;
        System.out.printf("Collecting data from page URL: %s", url);
        try {
            page = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.err.printf("Error occurred while connecting to page URL: %s.", url);
            // TODO: add to error links
            return null;
        }
        List<String> pageParseResult = new ArrayList<>();
        for (ElementParser elementParser : elementParsers) {
            pageParseResult.add(elementParser.parseByParameters(page));
        }
        return pageParseResult;
    }

    @Observed
    private Document getNextPage(Document page, String cssSelectorNextPage) throws IOException {
        String nextPageLink = page.select(cssSelectorNextPage).first().getElementsByAttribute("href").text();
        return Jsoup.connect(nextPageLink).get();
    }
}

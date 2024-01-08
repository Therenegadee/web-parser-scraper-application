package parser.app.webscraper.models;


import lombok.*;

import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserParserSetting extends FolderItem {
    private Long id;
    private String firstPageUrl;
    private int numOfPagesToParse;
    private String className; // класс, содержащий в себе ссылкий на страницы
    private String tagName; // тэг, уточняющий класс
    private String cssSelectorNextPage; // CSS Selector кнопки переключения страниц
    private List<String> header;
    private List<ElementLocator> elementLocators;
    private List<ParserResult> parsingHistory;
    private Folder parentFolder;
}

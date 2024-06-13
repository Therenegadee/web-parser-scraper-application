package parser.app.webscraper.models;


import com.mongodb.lang.Nullable;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ParsingPreset extends StorageItem {
    private String firstPageUrl;
    private int numOfPagesToParse;
    private String className; // класс, содержащий в себе ссылкий на страницы
    private String tagName; // тэг, уточняющий класс
    private String cssSelectorNextPage; // CSS Selector кнопки переключения страниц
    private List<String> header;
    private List<ElementLocator> elementLocators;
    private List<ParsingResult> parsingHistory;
    @Nullable
    private ObjectId parentFolderId;
    private String storageId;

    @Builder
    public ParsingPreset(
            ObjectId id,
            String name,
            List<String> tags,
            int numOfPagesToParse,
            String className,
            String tagName,
            String cssSelectorNextPage,
            List<String> header,
            List<ElementLocator> elementLocators,
            List<ParsingResult> parsingHistory,
            @Nullable ObjectId parentFolderId,
            String storageId
    ) {
        super(id, name, tags);
        this.numOfPagesToParse = numOfPagesToParse;
        this.className = className;
        this.tagName = tagName;
        this.cssSelectorNextPage = cssSelectorNextPage;
        this.header = header;
        this.elementLocators = elementLocators;
        this.parsingHistory = parsingHistory;
        this.parentFolderId = parentFolderId;
        this.storageId = storageId;
    }


    public void addParserResult(ParsingResult parsingResult) {
        parsingHistory.add(parsingResult);
    }
}

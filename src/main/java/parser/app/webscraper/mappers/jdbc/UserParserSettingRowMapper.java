package parser.app.webscraper.mappers.jdbc;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import parser.app.webscraper.dao.jdbc.interfaces.ElementLocatorDao;
import parser.app.webscraper.dao.jdbc.interfaces.FolderDao;
import parser.app.webscraper.dao.jdbc.interfaces.ParserResultDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.*;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserParserSettingRowMapper implements RowMapper<UserParserSetting> {

    @Autowired
    private ParserResultDao parserResultDao;
    @Autowired
    private ElementLocatorDao elementLocatorDao;
    private Map<Long, Folder> folderMap;
    private List<StorageItem> storageItems;
    private Storage storage;

    @Override
    public UserParserSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
        Array headerArr = rs.getArray("header");
        List<String> header = getListOfElements(headerArr);

        Array tagsArr = rs.getArray("tags");
        List<String> tags = getListOfElements(tagsArr);


        Long id = rs.getLong("id");

        UserParserSetting userParserSetting = UserParserSetting
                .builder()
                .id(id)
                .firstPageUrl(rs.getString("first_page_url"))
                .numOfPagesToParse(rs.getInt("num_of_pages_to_parse"))
                .className(rs.getString("class_name"))
                .tagName(rs.getString("tag_name"))
                .cssSelectorNextPage(rs.getString("css_selector_next_page"))
                .header(header)
                .elementLocators(getElementLocatorsById(id))
                .parsingHistory(getParsingHistoryById(id))
                .build();

        userParserSetting.setName(rs.getString("name"));
        userParserSetting.setTags(tags);

        if (Objects.nonNull(folderMap) && Objects.nonNull(storageItems) && Objects.nonNull(storage)) {
            long parentFolderId = rs.getLong("parent_folder_id");
            if (parentFolderId == 0) {
                userParserSetting.setParentFolder(null);
                storageItems.add(userParserSetting);
            } else {
                userParserSetting.setParentFolder(folderMap.get(parentFolderId));
            }
            userParserSetting.setStorage(storage);
        }
        return userParserSetting;
    }

    private List<String> getListOfElements(Array elementsArray) throws SQLException {
        List<String> outputList ;
        if (elementsArray != null) {
            String[] stringArray = (String[]) elementsArray.getArray();
            outputList = Arrays.asList(stringArray);
        } else {
            throw new IllegalArgumentException("Header couldn't be empty!");
        }
        return outputList;
    }

    private List<ParserResult> getParsingHistoryById(Long id) {
        return parserResultDao
                .findAllByParserSettingsId(id);
    }

    private List<ElementLocator> getElementLocatorsById(Long id) {
        return elementLocatorDao
                .findAllByParserSettingsId(id);
    }

    public UserParserSettingRowMapper(Map<Long, Folder> folderMap,
                                      List<StorageItem> storageItems,
                                      Storage storage) {
        this.folderMap = folderMap;
        this.storageItems = storageItems;
        this.storage = storage;
    }
}

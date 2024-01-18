package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import parser.app.webscraper.dao.jdbc.interfaces.ElementLocatorDao;
import parser.app.webscraper.dao.jdbc.interfaces.FolderDao;
import parser.app.webscraper.dao.jdbc.interfaces.ParserResultDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.ParserResult;
import parser.app.webscraper.models.UserParserSetting;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserParserSettingRowMapper implements RowMapper<UserParserSetting> {

    private final FolderDao folderDao;
    private final ParserResultDao parserResultDao;
    private final ElementLocatorDao elementLocatorDao;

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
                .parentFolder(getParentFolderByParentFolderId(rs.getLong("parent_folder_id")))
                .parsingHistory(getParsingHistoryById(id))
                .build();

        userParserSetting.setName(rs.getString("name"));
        userParserSetting.setTags(tags);
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

    private Folder getParentFolderByParentFolderId(Long parentFolderId) {
        return folderDao
                .findByFolderId(parentFolderId)
                .orElseThrow(() -> new NotFoundException(String.format("Parent Folder with id %d Wasn't Found", parentFolderId)));
    }

    private List<ParserResult> getParsingHistoryById(Long id) {
        return parserResultDao
                .findAllByParserSettingsId(id);
    }

    private List<ElementLocator> getElementLocatorsById(Long id) {
        return elementLocatorDao
                .findAllByParserSettingsId(id);
    }
}

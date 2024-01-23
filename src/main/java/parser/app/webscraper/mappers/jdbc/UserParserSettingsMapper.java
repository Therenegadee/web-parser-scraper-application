package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.*;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserParserSettingsMapper implements ResultSetExtractor<List<UserParserSetting>> {
    private final JdbcTemplate jdbcTemplate;
    private final ElementLocatorRowMapper elementLocatorMapper;
    private final ParserResultRowMapper parserResultMapper;
    private final Folder parentFolder = new Folder();
    private Map<Long, UserParserSetting> userParserSettingMap;
    private final Storage storage = new Storage();

    @Override
    public List<UserParserSetting> extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (rs.isBeforeFirst()) {
            rs.next();
        }

        List<UserParserSetting> resultList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        while (!rs.isAfterLast()) {
            Long id = rs.getLong("id");
            UserParserSetting userParserSetting;

            ids.add(id);
            userParserSetting = new UserParserSetting();
            userParserSetting.setParsingHistory(new ArrayList<>());
            userParserSetting.setElementLocators(new ArrayList<>());
            userParserSetting.setId(id);
            userParserSetting.setName(rs.getString("name"));
            Array tagsArr = rs.getArray("tags");
            userParserSetting.setTags(getListOfElements(tagsArr));
            userParserSetting.setFirstPageUrl(rs.getString("first_page_url"));
            userParserSetting.setNumOfPagesToParse(rs.getInt("num_of_pages_to_parse"));
            userParserSetting.setClassName(rs.getString("class_name"));
            userParserSetting.setTagName(rs.getString("tag_name"));
            Array headerArr = rs.getArray("header");
            userParserSetting.setHeader(getListOfElements(headerArr));

            Long storageId = rs.getLong("storage_id");
            if (!Objects.equals(storage.getId(), storageId)) {
                storage.setId(storageId);
            }
            userParserSetting.setStorage(storage);

            Long folderId = rs.getLong("parent_folder_id");
            if (!Objects.equals(parentFolder.getId(), folderId)) {
                parentFolder.setId(folderId);
            }

            userParserSetting.setParentFolder(parentFolder);
            resultList.add(userParserSetting);
            userParserSettingMap.put(id, userParserSetting);
            rs.next();
        }

        List<ElementLocator> elementLocators = getElementLocators(ids);
        List<ParserResult> parserResults = getParsingHistory(ids);

        for(ElementLocator e : elementLocators) {
            //TODO: добавить в параметры метода мапу сеттингов
            userParserSettingMap.get(e.getUserParserSetting().getId()).getElementLocators().add(e);
        }
        for(ParserResult pr : parserResults) {
            //TODO: добавить в параметры метода мапу сеттингов
            userParserSettingMap.get(pr.getUserParserSetting().getId()).getParsingHistory().add(pr);
        }
        return resultList;
    }

    private List<String> getListOfElements(Array elementsArray) throws SQLException {
        List<String> outputList;
        if (elementsArray != null) {
            String[] stringArray = (String[]) elementsArray.getArray();
            outputList = Arrays.asList(stringArray);
        } else {
            throw new IllegalArgumentException("Array couldn't be empty!");
        }
        return outputList;
    }

    private List<ElementLocator> getElementLocators(List<Long> settingsIds) {
        String query = "SELECT * FROM element_locator WHERE user_parser_settings_id IN (" +
                String.join(", ", Collections.nCopies(settingsIds.size(), "?")) +
                ")" +
                "ORDER BY user_parser_settings_id, id";
        return jdbcTemplate.query(query, elementLocatorMapper, settingsIds.toArray());
    }

    private List<ParserResult> getParsingHistory(List<Long> settingsIds) {
        String query = "SELECT * FROM parser_results WHERE user_parser_settings_id IN (" +
                String.join(", ", Collections.nCopies(settingsIds.size(), "?")) +
                ")" +
                "ORDER BY user_parser_settings_id, id";

        return jdbcTemplate.query(query, parserResultMapper, settingsIds.toArray());
    }

}

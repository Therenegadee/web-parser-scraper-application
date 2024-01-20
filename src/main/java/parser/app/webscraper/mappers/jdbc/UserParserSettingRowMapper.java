package parser.app.webscraper.mappers.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.*;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserParserSettingRowMapper implements RowMapper<UserParserSetting> {
    private final Map<Long, Folder> folderMap;
    private Map<Long, UserParserSetting> userParserSettingMap;
    private List<StorageItem> storageItems;
    private final Folder parentFolder = new Folder();
    private Storage storage = new Storage();
    private boolean isForStorageMap = false;
    private boolean isForFolderMap = false;


    public UserParserSettingRowMapper(Map<Long, Folder> folderMap,
                                      List<StorageItem> storageItems,
                                      Storage storage) {
        this.folderMap = folderMap;
        this.storageItems = storageItems;
        this.storage = storage;
        this.isForStorageMap = true;
    }

    public UserParserSettingRowMapper(Map<Long, Folder> folderMap) {
        this.folderMap = folderMap;
        this.isForFolderMap = true;
    }

    @Override
    public UserParserSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
        if (isForStorageMap) {
            return mapForStorage(rs);
        } else if (isForFolderMap) {
            return mapForFolder(rs);
        } else {
            return map(rs);
        }
    }

    private UserParserSetting map(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        UserParserSetting userParserSetting;
        if (!userParserSettingMap.containsKey(id)) {
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
        } else {
            userParserSetting = userParserSettingMap.get(id);
        }
        userParserSetting.getElementLocators();
        userParserSetting.getParsingHistory();
        return userParserSetting;
    }

    private UserParserSetting mapForFolder(ResultSet rs) throws SQLException {

    }


    private UserParserSetting mapForStorage(ResultSet rs) throws SQLException {
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

    //TODO: чек на empty только для header
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

}

package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.models.StorageItem;
import parser.app.webscraper.models.UserParserSetting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class StorageRowMapper implements RowMapper<Storage> {

    @Override
    public Storage mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<Long, Folder> folderMap = new HashMap<>();
        Map<Long, UserParserSetting> settingsMap = new HashMap<>();
        List<StorageItem> storageItems = new ArrayList<>();

        Storage storage = new Storage();

        while (rs.next()) {
            long folderId = rs.getLong("folder_id");
            long settingsId = rs.getLong("settings_id");

            Folder folder = folderMap.get(folderId);
            if (folder == null) {
                folder = new Folder();
                folder.setName(rs.getString("folder_name"));
                folder.setTags(Arrays.asList((String[]) rs.getArray("folder_tags").getArray()));
                folder.setParentFolder(folderMap.get(rs.getLong("parent_folder_id")));

                if (folderId == 0) {
                    folder.setId(null);
                    storageItems.add(folder);
                } else {
                    folder.setId(folderId);
                    folderMap.get(folderId).addStorageItem(folder);
                }
                folder.setStorage(storage);
                folderMap.put(folderId, folder);
            }

            if (settingsId != 0) {
                UserParserSetting userParserSetting = settingsMap.get(settingsId);
                long parentFolderId = rs.getLong("settings_parent_folder_id");
                if (userParserSetting == null) {
                    userParserSetting = new UserParserSetting();
                    userParserSetting.setId(settingsId);
                    userParserSetting.setName(rs.getString("settings_name"));
                    userParserSetting.setTags(Arrays.asList((String[]) rs.getArray("settings_tags").getArray()));
                    userParserSetting.setFirstPageUrl(rs.getString("first_page_url"));
                    userParserSetting.setNumOfPagesToParse(rs.getInt("num_of_pages_to_parse"));
                    userParserSetting.setClassName(rs.getString("class_name"));
                    userParserSetting.setTagName(rs.getString("tag_name"));
                    userParserSetting.setCssSelectorNextPage(rs.getString("css_selector_next_page"));
                    userParserSetting.setHeader(Arrays.asList((String[]) rs.getArray("header").getArray()));
                }
                if(parentFolderId == 0) {
                    userParserSetting.setParentFolder(null);
                    storageItems.add(userParserSetting);
                } else {
                    userParserSetting.setParentFolder(folderMap.get(parentFolderId));
                    folderMap.get(parentFolderId).addStorageItem(userParserSetting);
                }
                userParserSetting.setStorage(storage);
                settingsMap.put(settingsId, userParserSetting);
            }
        }
        storage.setStorageItems(storageItems);
        return storage;
    }
}

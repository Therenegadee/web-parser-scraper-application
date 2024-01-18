package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
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
public class StorageMapper implements ResultSetExtractor<Storage> {

    @Override
    public Storage extractData(ResultSet rs) throws SQLException, DataAccessException {
        if(rs.isBeforeFirst()) {
            rs.next();
        }

        Map<Long, Folder> folderMap = new HashMap<>();
        Map<Long, UserParserSetting> settingsMap = new HashMap<>();
        List<StorageItem> storageItems = new ArrayList<>();

        Storage storage = new Storage();
        storage.setId(rs.getLong("st.id"));
        storage.setUserId(rs.getLong("st.user_id"));

        while (!rs.isAfterLast()) {
            long folderId = rs.getLong("f.folder_id");
            long settingsId = rs.getLong("s.settings_id");

            Folder folder = folderMap.get(folderId);
            if (folder == null) {
                folder = new Folder();
                folder.setName(rs.getString("f.folder_name"));
                folder.setTags(Arrays.asList((String[]) rs.getArray("f.folder_tags").getArray()));
                folder.setParentFolder(folderMap.get(rs.getLong("f.parent_folder_id")));

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
                long parentFolderId = rs.getLong("s.parent_folder_id");
                if (userParserSetting == null) {
                    userParserSetting = new UserParserSetting();
                    userParserSetting.setId(settingsId);
                    userParserSetting.setName(rs.getString("s.name"));
                    userParserSetting.setTags(Arrays.asList((String[]) rs.getArray("s.tags").getArray()));
                    userParserSetting.setFirstPageUrl(rs.getString("s.first_page_url"));
                    userParserSetting.setNumOfPagesToParse(rs.getInt("s.num_of_pages_to_parse"));
                    userParserSetting.setClassName(rs.getString("s.class_name"));
                    userParserSetting.setTagName(rs.getString("s.tag_name"));
                    userParserSetting.setCssSelectorNextPage(rs.getString("s.css_selector_next_page"));
                    userParserSetting.setHeader(Arrays.asList((String[]) rs.getArray("s.header").getArray()));
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
            rs.next();
        }
        storage.setStorageItems(storageItems);
        return storage;
    }

    f
}

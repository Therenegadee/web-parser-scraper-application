package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.models.StorageItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class StorageMapper implements ResultSetExtractor<Storage> {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_TO_SETTINGS = """
            SELECT *
            FROM user_parser_settings
            WHERE parent_folder_id IS NULL OR id BETWEEN ? AND ?
            ORDER BY parent_folder_id NULLS FIRST, id;
            """;

    @Override
    public Storage extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (rs.isBeforeFirst()) {
            rs.next();
        }

        long userId = rs.getLong("user_id");
        long minId = Long.MAX_VALUE;
        long maxId = Long.MIN_VALUE;

        Map<Long, Folder> folderMap = new HashMap<>();
        List<StorageItem> storageItems = new ArrayList<>();

        Storage storage = new Storage();
        storage.setId(rs.getLong("storage_id"));
        storage.setUserId(userId);

        while (!rs.isAfterLast()) {
            Folder folder = new Folder();
            long id = rs.getLong("id");

            maxId = Math.max(maxId, id);
            minId = Math.min(minId, id);

            folder.setId(id);
            folder.setName(rs.getString("name"));
            folder.setTags(Arrays.asList((String[]) rs.getArray("tags").getArray()));
            folder.setStorage(storage);

            Long parentFolderId = rs.getLong("parent_folder_id");
            if (parentFolderId == 0) {
                folder.setParentFolder(null);
                storageItems.add(folder);
            } else {
                folder.setParentFolder(folderMap.get(parentFolderId));
                folderMap.get(parentFolderId).addStorageItem(folder);
            }

            folderMap.put(parentFolderId, folder);
        }

        jdbcTemplate.query(
                SQL_TO_SETTINGS,
                new UserParserSettingRowMapper(folderMap, storageItems, storage),
                minId,
                maxId);

        storage.setStorageItems(storageItems);
        return storage;
    }

}

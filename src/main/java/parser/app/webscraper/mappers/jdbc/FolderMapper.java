package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import parser.app.webscraper.dao.jdbc.interfaces.FolderDao;
import parser.app.webscraper.dao.jdbc.interfaces.UserParserSettingsDao;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.UserParserSetting;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FolderMapper implements ResultSetExtractor<List<Folder>> {
    private final FolderDao folderDao;
    private final UserParserSettingsDao userParserSettingsDao;
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_TO_SETTINGS = """
            SELECT *
            FROM user_parser_settings
            WHERE storage_id = ? AND parent_folder_id BETWEEN ? AND ?
            ORDER BY parent_folder_id NULLS FIRST, id;
                        """;


    @Override
    public List<Folder> extractData(ResultSet rs) throws SQLException {
        if (rs.isBeforeFirst()) {
            rs.next();
        }

        HashMap<Long, Folder> folders = new HashMap<>();
        List<Folder> resultFolderList = new ArrayList<>();
        long minId = Long.MAX_VALUE;
        long maxId = Long.MIN_VALUE;

        while (!rs.isAfterLast()) {
            long folderId = rs.getLong("id");
            maxId = Math.max(maxId, folderId);
            minId = Math.min(minId, folderId);

            Long parentFolderId = rs.getLong("parent_folder_id");

            Folder folder = new Folder();
            folder.setId(folderId);

            if (parentFolderId == 0) {
                folder.setParentFolder(null);
            } else {
                folder.setParentFolder(folders.get(parentFolderId));
                folders.get(parentFolderId).addFolderItem(folder);
            }
            folder.setName(rs.getString("name"));
            Array tagsArr = rs.getArray("tags");
            folder.setTags(getListOfElements(tagsArr));
            resultFolderList.add(folder);
            rs.next();
        }


        return folder;
    }

    private List<String> getListOfElements(Array elementsArray) throws SQLException {
        List<String> outputList;
        if (elementsArray != null) {
            String[] stringArray = (String[]) elementsArray.getArray();
            outputList = Arrays.asList(stringArray);
        } else {
            throw new IllegalArgumentException("Header couldn't be empty!");
        }
        return outputList;
    }

    private List<UserParserSetting> findSettings(HashMap<Long, Folder> folders, Long storageId, List<Long> folderIds, long minId, long maxId) {
       return jdbcTemplate.query(
               SQL_TO_SETTINGS,
               new UserParserSettingRowMapper(folders),
               storageId,
               minId,
               maxId);
    }


}

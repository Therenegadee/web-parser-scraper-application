package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import parser.app.webscraper.dao.jdbc.interfaces.FolderDao;
import parser.app.webscraper.dao.jdbc.interfaces.UserParserSettingsDao;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.StorageItem;
import parser.app.webscraper.models.UserParserSetting;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FolderRowMapper implements RowMapper<Folder> {
    private final FolderDao folderDao;
    private final UserParserSettingsDao userParserSettingsDao;


    @Override
    public Folder mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        List<StorageItem> storageItems = findItems(id);
        Folder folder = Folder.builder()
                .id(id)
                .parentFolder(findParentFolder(rs.getLong("parent_folder_id")).orElse(null))
                .storageItems(storageItems)
                .build();
        folder.setName(rs.getString("name"));
        Array tagsArr = rs.getArray("tags");
        folder.setTags(getListOfElements(tagsArr));
        return folder;
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


}

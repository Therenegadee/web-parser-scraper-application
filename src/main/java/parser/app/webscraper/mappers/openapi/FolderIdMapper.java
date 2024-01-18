package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.dao.interfaces.FolderDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.Folder;

import java.util.Objects;

@Mapper(componentModel = "spring")
public class FolderIdMapper {

    @Autowired
    private FolderDao folderDao;

    public Folder getFolder(Long id) {
        if (id == null) {
            return null;
        }
        return folderDao.findByFolderId(id)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %d wasn't found", id)));
    }

    public Long getId(Folder folder){
        if (Objects.isNull(folder)) {
            return null;
        }
        return folder.getId();
    }
}

package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.repository.StorageRepository;

import java.util.Objects;
import java.util.UUID;

@Mapper(componentModel = "spring")
public class FolderIdMapper {

    @Autowired
    private StorageRepository storageRepository;

    public Folder getFolder(UUID id, UUID storageId) {
        if (id == null) {
            return null;
        }
        return (Folder) storageRepository.findFolderById(storageId, id)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %d wasn't found", id)));
    }

    public UUID getId(Folder folder){
        if (Objects.isNull(folder)) {
            return null;
        }
        return folder.getId();
    }
}

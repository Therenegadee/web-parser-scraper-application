package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.mappers.ObjectIdMapper;
import parser.app.webscraper.models.Storage;
import parser.userService.openapi.model.StorageDTO;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StorageItemMapper.class, ObjectIdMapper.class})
public interface StorageMapper {
    Storage toStorage(StorageDTO storageDTO);

    List<Storage> toStorage(List<StorageDTO> storageDTO);

    StorageDTO toDTO(Storage storage);

    List<StorageDTO> toDTO(List<Storage> storage);
}

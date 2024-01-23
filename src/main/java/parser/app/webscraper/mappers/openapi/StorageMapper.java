package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.Storage;
import parser.userService.openapi.model.StorageOpenApi;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StorageItemMapper.class})
public interface StorageMapper {
    Storage toStorage(StorageOpenApi storageOpenApi);
    List<Storage> toStorage(List<StorageOpenApi> storageOpenApi);
    StorageOpenApi toOpenApi(Storage storage);
    List<StorageOpenApi> toOpenApi(List<Storage> storage);
}

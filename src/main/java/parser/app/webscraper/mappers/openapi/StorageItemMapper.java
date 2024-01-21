package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.StorageItem;
import parser.userService.openapi.model.StorageItemOpenApi;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FolderMapper.class, UserParserSettingsMapper.class})
public interface StorageItemMapper {
//    StorageItem toStorageItem(StorageItemOpenApi folderItemOpenApi);
//
//    List<StorageItem> toStorageItem(List<StorageItemOpenApi> folderItemOpenApi);
//
//    StorageItemOpenApi toOpenApi(StorageItem storageItem);
//
//    List<StorageItemOpenApi> toOpenApi(List<StorageItem> storageItem);
}

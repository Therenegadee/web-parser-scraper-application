package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.StorageItem;
import parser.userService.openapi.model.FolderItemOpenApi;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FolderMapper.class, UserParserSettingsMapper.class})
public interface FolderItemMapper {
    StorageItem toFolderItem(FolderItemOpenApi folderItemOpenApi);

    List<StorageItem> toFolderItem(List<FolderItemOpenApi> folderItemOpenApi);

    FolderItemOpenApi toOpenApi(StorageItem storageItem);

    List<FolderItemOpenApi> toOpenApi(List<StorageItem> storageItem);
}

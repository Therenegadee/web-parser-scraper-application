package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.StorageItem;
import parser.app.webscraper.models.UserParserSetting;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageItemOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {FolderMapper.class, UserParserSettingsMapper.class})
public interface StorageItemMapper {
    default StorageItem toStorageItem(StorageItemOpenApi storageItemOpenApi){
        if (storageItemOpenApi instanceof FolderOpenApi) {
            return toFolder((FolderOpenApi) storageItemOpenApi);
        } else if (storageItemOpenApi instanceof UserParserSettingsOpenApi) {
            return toUserParserSetting((UserParserSettingsOpenApi) storageItemOpenApi);
        } else return null;
    }

    default List<StorageItem> toStorageItem(List<StorageItemOpenApi> storageItemOpenApi){
        return storageItemOpenApi.stream()
                .map(this::toStorageItem)
                .collect(Collectors.toList());
    }

    default StorageItemOpenApi toOpenApi(StorageItem storageItem) {
        if (storageItem instanceof Folder) {
            return toOpenApi((Folder) storageItem);
        } else if (storageItem instanceof UserParserSetting) {
            return toOpenApi((UserParserSetting) storageItem);
        } else {
            return null;
        }
    }

    default List<StorageItemOpenApi> toOpenApi(List<StorageItem> storageItemList) {
        return storageItemList.stream()
                .map(this::toOpenApi)
                .collect(Collectors.toList());
    }

    Folder toFolder(StorageItemOpenApi folderItemOpenApi);

    List<Folder> toFolder(List<StorageItemOpenApi> folderItemOpenApi);

    UserParserSetting toUserParserSetting(StorageItemOpenApi userParserSettingOpenApi);

    List<UserParserSetting> toUserParserSetting(List<StorageItemOpenApi> userParserSettingOpenApi);
}

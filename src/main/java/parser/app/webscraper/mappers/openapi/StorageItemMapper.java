package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.models.StorageItem;
import parser.userService.openapi.model.FolderDTO;
import parser.userService.openapi.model.ParsingPresetDTO;
import parser.userService.openapi.model.StorageItemDTO;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        FolderMapper.class,
        StorageMapper.class,
        ParsingPresetMapper.class,
})
public interface StorageItemMapper {
    default StorageItem toStorageItem(StorageItemDTO storageItemOpenApi){
        if (storageItemOpenApi instanceof FolderDTO) {
            return toFolder(storageItemOpenApi);
        } else if (storageItemOpenApi instanceof ParsingPresetDTO) {
            return toParsingPreset(storageItemOpenApi);
        } else return null;
    }

    default List<StorageItem> toStorageItem(List<StorageItemDTO> storageItemOpenApi){
        return storageItemOpenApi.stream()
                .map(this::toStorageItem)
                .collect(Collectors.toList());
    }

    default StorageItemDTO toDTO(StorageItem storageItem) {
        if (storageItem instanceof Folder) {
            return toDTO(storageItem);
        } else if (storageItem instanceof ParsingPreset) {
            return toDTO(storageItem);
        } else {
            return null;
        }
    }

    default List<StorageItemDTO> toDTO(List<StorageItem> storageItemList) {
        return storageItemList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    Folder toFolder(StorageItemDTO folderItemOpenApi);

    List<Folder> toFolder(List<StorageItemDTO> folderItemOpenApi);

    ParsingPreset toParsingPreset(StorageItemDTO userParserSettingOpenApi);

    List<ParsingPreset> toParsingPreset(List<StorageItemDTO> userParserSettingOpenApi);
}

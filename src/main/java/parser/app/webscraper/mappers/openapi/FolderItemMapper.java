package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.FolderItem;
import parser.userService.openapi.model.FolderItemOpenApi;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FolderItemMapper {
    FolderItem toFolderItem(FolderItemOpenApi folderItemOpenApi);
    List<FolderItem> toFolderItem(List<FolderItemOpenApi> folderItemOpenApi);
    FolderItemOpenApi toOpenApi(FolderItem folderItem);
    List<FolderItemOpenApi> toOpenApi(List<FolderItem> folderItem);
}

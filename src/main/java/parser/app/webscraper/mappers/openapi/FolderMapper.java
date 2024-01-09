package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.Folder;
import parser.userService.openapi.model.FolderOpenApi;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    Folder toFolder(FolderOpenApi folderOpenApi);

    List<Folder> toFolder(List<FolderOpenApi> folderOpenApi);

    FolderOpenApi toOpenApi(Folder folder);

    List<FolderOpenApi> toOpenApi(List<Folder> folder);
}

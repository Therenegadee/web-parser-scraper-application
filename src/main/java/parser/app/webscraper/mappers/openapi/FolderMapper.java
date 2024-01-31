package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.models.Folder;
import parser.userService.openapi.model.FolderOpenApi;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {StorageItemMapper.class})
public interface FolderMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    Folder toFolder(FolderOpenApi folderOpenApi);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<Folder> toFolder(List<FolderOpenApi> folderOpenApi);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    FolderOpenApi toOpenApi(Folder folder);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<FolderOpenApi> toOpenApi(List<Folder> folder);

}

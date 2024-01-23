package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.models.Folder;
import parser.userService.openapi.model.FolderOpenApi;

import java.util.List;

@Mapper(componentModel = "spring"
// ,
//        uses = {FolderIdMapper.class}
)
public interface FolderMapper {

    @Mapping(source = "parentFolderId", target = "parentFolderId")
    Folder toFolder(FolderOpenApi folderOpenApi);

    @Mapping(source = "parentFolderId", target = "parentFolderId")
    List<Folder> toFolder(List<FolderOpenApi> folderOpenApi);

    @Mapping(source = "parentFolderId", target = "parentFolderId")
    FolderOpenApi toOpenApi(Folder folder);

    @Mapping(source = "parentFolderId", target = "parentFolderId")
    List<FolderOpenApi> toOpenApi(List<Folder> folder);



}

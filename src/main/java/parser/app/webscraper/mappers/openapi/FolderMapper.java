package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.models.Folder;
import parser.userService.openapi.model.FolderDTO;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {StorageItemMapper.class})
public interface FolderMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    Folder toFolder(FolderDTO folderDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<Folder> toFolder(List<FolderDTO> folderDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    FolderDTO toDTO(Folder folder);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<FolderDTO> toDTO(List<Folder> folder);

}

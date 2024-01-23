package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.mappers.DateMapper;
import parser.app.webscraper.models.UserParserSetting;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class, ParserResultMapper.class, ElementLocatorMapper.class, FolderIdMapper.class}
)
public interface UserParserSettingsMapper {

    @Mapping(source = "parentFolderId", target = "parentFolderId")
    UserParserSetting toUserParseSetting(UserParserSettingsOpenApi userParserSettingsOpenApi);

    @Mapping(source = "parentFolderId", target = "parentFolderId")
    List<UserParserSetting> toUserParseSetting(List<UserParserSettingsOpenApi> userParserSettingsOpenApi);

    @Mapping(source = "parentFolderId", target = "parentFolderId")
    UserParserSettingsOpenApi toOpenApi(UserParserSetting userParserSetting);

    @Mapping(source = "parentFolderId", target = "parentFolderId")
    List<UserParserSettingsOpenApi> toOpenApi(List<UserParserSetting> userParserSettingsOpenApi);

}

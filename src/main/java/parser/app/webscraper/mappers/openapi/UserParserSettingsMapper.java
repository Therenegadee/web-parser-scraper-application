package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.mappers.DateMapper;
import parser.app.webscraper.models.UserParserSetting;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {DateMapper.class, ParserResultMapper.class, ElementLocatorMapper.class}
)
public interface UserParserSettingsMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    UserParserSetting toUserParseSetting(UserParserSettingsOpenApi userParserSettingsOpenApi);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<UserParserSetting> toUserParseSetting(List<UserParserSettingsOpenApi> userParserSettingsOpenApi);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    UserParserSettingsOpenApi toOpenApi(UserParserSetting userParserSetting);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<UserParserSettingsOpenApi> toOpenApi(List<UserParserSetting> userParserSettingsOpenApi);

}

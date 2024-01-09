package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.mappers.DateMapper;
import parser.app.webscraper.models.UserParserSetting;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class, ParserResultMapper.class, ElementLocatorMapper.class}
)
public interface UserParserSettingsMapper {
    UserParserSetting toUserParseSetting(UserParserSettingsOpenApi userParserSettingsOpenApi);

    List<UserParserSetting> toUserParseSetting(List<UserParserSettingsOpenApi> userParserSettingsOpenApi);

    UserParserSettingsOpenApi toOpenApi(UserParserSetting userParserSetting);

    List<UserParserSettingsOpenApi> toOpenApi(List<UserParserSetting> userParserSettingsOpenApi);

}

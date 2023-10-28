package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.UserParserSetting;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

@Mapper(componentModel = "spring")
public interface UserParserSettingsMapper {
    UserParserSetting toUserParseSetting(UserParserSettingsOpenApi userParserSettingsOpenApi);
    UserParserSettingsOpenApi toOpenApi(UserParserSetting userParserSetting);
}

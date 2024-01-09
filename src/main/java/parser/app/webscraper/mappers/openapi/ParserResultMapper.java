package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.mappers.DateMapper;
import parser.app.webscraper.models.ParserResult;
import parser.userService.openapi.model.ParserResultOpenApi;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {DateMapper.class, UserParserSettingsIdMapper.class})
public interface ParserResultMapper {
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    ParserResult toParserResult(ParserResultOpenApi parserResultOpenApi);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    ParserResultOpenApi toOpenApi(ParserResult parserResult);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    List<ParserResult> toParserResult(List<ParserResultOpenApi> parserResultsOpenApi);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    List<ParserResultOpenApi> toOpenApi(List<ParserResult> parserResults);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Set<ParserResult> toParserResult(Set<ParserResultOpenApi> parserResultsOpenApi);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Set<ParserResultOpenApi> toOpenApi(Set<ParserResult> parserResults);
}

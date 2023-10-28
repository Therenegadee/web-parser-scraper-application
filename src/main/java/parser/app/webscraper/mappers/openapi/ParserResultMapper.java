package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.ParserResult;
import parser.userService.openapi.model.ParserResultOpenApi;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ParserResultMapper {
    ParserResult toParserResult(ParserResultOpenApi parserResultOpenApi);
    ParserResultOpenApi toOpenApi(ParserResult parserResult);

    List<ParserResult> toParserResult(List<ParserResultOpenApi> parserResultsOpenApi);
    List<ParserResultOpenApi> toOpenApi(List<ParserResult> parserResults);
    Set<ParserResult> toParserResult(Set<ParserResultOpenApi> parserResultsOpenApi);
    Set<ParserResultOpenApi> toOpenApi(Set<ParserResult> parserResults);
}

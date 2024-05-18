package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.mappers.DateMapper;
import parser.app.webscraper.mappers.ObjectIdMapper;
import parser.app.webscraper.models.ParserResult;
import parser.userService.openapi.model.ParserResultDTO;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {DateMapper.class, ObjectIdMapper.class})
public interface ParserResultMapper {
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    ParserResult toParserResult(ParserResultDTO parserResultDTO);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    ParserResultDTO toDTO(ParserResult parserResult);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    List<ParserResult> toParserResult(List<ParserResultDTO> parserResultsDTO);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    List<ParserResultDTO> toDTO(List<ParserResult> parserResults);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Set<ParserResult> toParserResult(Set<ParserResultDTO> parserResultsDTO);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Set<ParserResultDTO> toDTO(Set<ParserResult> parserResults);
}

package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.mappers.DateMapper;
import parser.app.webscraper.mappers.ObjectIdMapper;
import parser.app.webscraper.models.ParsingResult;
import parser.userService.openapi.model.ParsingResultDTO;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {DateMapper.class, ObjectIdMapper.class})
public interface ParsingResultMapper {
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    ParsingResult toParserResult(ParsingResultDTO parserResultDTO);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    ParsingResultDTO toDTO(ParsingResult parsingResult);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    List<ParsingResult> toParserResult(List<ParsingResultDTO> parserResultsDTO);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    List<ParsingResultDTO> toDTO(List<ParsingResult> parsingResults);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Set<ParsingResult> toParserResult(Set<ParsingResultDTO> parserResultsDTO);
    @Mapping(target = "date", source = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Set<ParsingResultDTO> toDTO(Set<ParsingResult> parsingResults);
}

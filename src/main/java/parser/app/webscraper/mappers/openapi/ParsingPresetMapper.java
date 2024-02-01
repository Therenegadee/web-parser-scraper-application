package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.mappers.DateMapper;
import parser.app.webscraper.models.ParsingPreset;
import parser.userService.openapi.model.ParsingPresetDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {DateMapper.class,
                ParserResultMapper.class,
                ElementLocatorMapper.class,}
)
public interface ParsingPresetMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    ParsingPreset toUserParseSetting(ParsingPresetDTO parsingPresetDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<ParsingPreset> toUserParseSetting(List<ParsingPresetDTO> parsingPresetDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    ParsingPresetDTO toDTO(ParsingPreset parsingPreset);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<ParsingPresetDTO> toDTO(List<ParsingPreset> userParserSettingsDTO);

}

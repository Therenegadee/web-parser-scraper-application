package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.ComponentScan;
import parser.app.webscraper.mappers.DateMapper;
import parser.app.webscraper.mappers.ObjectIdMapper;
import parser.app.webscraper.models.ParsingPreset;
import parser.userService.openapi.model.ParsingPresetDTO;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {DateMapper.class,
                ParsingResultMapper.class,
                ElementLocatorMapper.class,
                ObjectIdMapper.class
        }
)
@ComponentScan(basePackages = "src/main/java/parser/app/webscraper/mappers")
public interface ParsingPresetMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    ParsingPreset toParsingPreset(ParsingPresetDTO parsingPresetDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<ParsingPreset> toParsingPreset(List<ParsingPresetDTO> parsingPresetDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    ParsingPresetDTO toDTO(ParsingPreset parsingPreset);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "tags", target = "tags")
    List<ParsingPresetDTO> toDTO(List<ParsingPreset> userParserSettingsDTO);

}

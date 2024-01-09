package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.ElementLocator;
import parser.userService.openapi.model.ElementLocatorOpenApi;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserParserSettingsIdMapper.class})
public interface ElementLocatorMapper {
    ElementLocator toElementLocator(ElementLocatorOpenApi elementLocatorOpenApi);

    List<ElementLocator> toElementLocator(List<ElementLocatorOpenApi> elementLocatorOpenApi);

    ElementLocatorOpenApi toOpenApi(ElementLocator elementLocator);

    List<ElementLocatorOpenApi> toOpenApi(List<ElementLocator> elementLocator);

}

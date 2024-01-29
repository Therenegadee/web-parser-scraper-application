package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.models.ElementLocator;
import parser.userService.openapi.model.ElementLocatorOpenApi;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ElementLocatorMapper {

    @Mapping(source = "elementType", target = "type")
    ElementLocator toElementLocator(ElementLocatorOpenApi elementLocatorOpenApi);

    @Mapping(source = "elementType", target = "type")
    List<ElementLocator> toElementLocator(List<ElementLocatorOpenApi> elementLocatorOpenApi);

    @Mapping(source = "type", target = "elementType")
    ElementLocatorOpenApi toOpenApi(ElementLocator elementLocator);

    @Mapping(source = "type", target = "elementType")
    List<ElementLocatorOpenApi> toOpenApi(List<ElementLocator> elementLocator);

}

package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import parser.app.webscraper.models.ElementLocator;
import parser.userService.openapi.model.ElementLocatorOpenApi;

@Mapper(componentModel = "spring")
public interface ElementLocatorMapper {
    ElementLocator toElementLocator(ElementLocatorOpenApi elementLocatorOpenApi);
    ElementLocatorOpenApi toOpenApi(ElementLocator elementLocator);
}

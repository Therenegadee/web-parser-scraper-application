package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import parser.app.webscraper.models.ElementLocator;
import parser.userService.openapi.model.ElementLocatorDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ElementLocatorMapper {

    @Mapping(source = "elementType", target = "type")
    ElementLocator toElementLocator(ElementLocatorDTO elementLocatorDTO);

    @Mapping(source = "elementType", target = "type")
    List<ElementLocator> toElementLocator(List<ElementLocatorDTO> elementLocatorDTO);

    @Mapping(source = "type", target = "elementType")
    ElementLocatorDTO toDTO(ElementLocator elementLocator);

    @Mapping(source = "type", target = "elementType")
    List<ElementLocatorDTO> toDTO(List<ElementLocator> elementLocator);

}

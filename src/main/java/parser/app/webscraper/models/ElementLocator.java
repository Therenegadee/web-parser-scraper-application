package parser.app.webscraper.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import parser.app.webscraper.models.enums.ElementType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElementLocator {
    @Id
    private Long id;
    private String name;
    private ElementType type;
    private String pathToLocator;
    private String extraPointer; // attribute value (for Tag + Attribute)
    private String userParserSettingsId;
    private boolean isCountable;
    private ElementType countableElementType;
    private String pathToCountableLocator;
}

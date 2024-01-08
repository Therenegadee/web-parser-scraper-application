package parser.app.webscraper.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.ParserResultOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;
import java.util.Set;

public interface ParserService {
    List<ParserResultOpenApi> getAllParserQueries();

    ParserResultOpenApi showParserResultsById(Long id);

    ResponseEntity<Void> setParserSettings(UserParserSettingsOpenApi userParserSettingsOpenApi);

    ResponseEntity<Void> runParser(Long id);

    ResponseEntity<Resource> downloadFile(Long id);
}

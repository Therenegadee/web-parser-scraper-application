package parser.app.webscraper.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import parser.app.webscraper.models.ParserResult;
import parser.userService.openapi.model.ParserResultOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;
import java.util.Set;

public interface ParserService {
    ResponseEntity<Void> createParserSettings(Long userId, UserParserSettingsOpenApi userParserSettingsOpenApi);

    UserParserSettingsOpenApi getParserSettingsById(Long id);

    List<UserParserSettingsOpenApi> getAllParserSettingsByUserId(Long userId);

    ResponseEntity<Void> deleteParserSettingsById(Long id);

    ResponseEntity<Void> runParser(Long id, ParserResultOpenApi parserResultOpenApi);

    ResponseEntity<Resource> downloadFile(Long id);
}

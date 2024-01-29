package parser.app.webscraper.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.ParserResultOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;

public interface ParserService {
    ResponseEntity<Void> createParserSettings(Long userId, UserParserSettingsOpenApi userParserSettingsOpenApi);

    UserParserSettingsOpenApi findParserSettingsById(String storageId, String settingsId);

    ResponseEntity<Void> updateParserSettingsById(String storageId, String settingsId, UserParserSettingsOpenApi userParserSettingsOpenApi);

    ResponseEntity<Void> deleteParserSettingsById(String storageId, String settingsId);

    List<UserParserSettingsOpenApi> getAllParserSettingsByUserId(Long userId);


    ResponseEntity<Void> runParser(String storageId, String settingsId, ParserResultOpenApi parserResultOpenApi);

    ResponseEntity<Resource> downloadFile(String storageId, String settingsId, Long parserResultId);
}

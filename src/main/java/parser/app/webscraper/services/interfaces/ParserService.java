package parser.app.webscraper.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.ParserResultOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;
import java.util.UUID;

public interface ParserService {
    ResponseEntity<Void> createParserSettings(Long userId, UserParserSettingsOpenApi userParserSettingsOpenApi);

    ResponseEntity<Void> createParserSettings(Long userId, String folderName, UserParserSettingsOpenApi userParserSettingsOpenApi);

    UserParserSettingsOpenApi findParserSettingsById(UUID storageId, UUID settingsId);

    ResponseEntity<Void> updateParserSettingsById(UUID storageId, UUID settingsId, UserParserSettingsOpenApi userParserSettingsOpenApi);

    ResponseEntity<Void> deleteParserSettingsById(UUID storageId, UUID settingsId);

    List<UserParserSettingsOpenApi> getAllParserSettingsByUserId(Long userId);


    ResponseEntity<Void> runParser(UUID storageId, UUID settingsId, ParserResultOpenApi parserResultOpenApi);

    ResponseEntity<Resource> downloadFile(UUID storageId, UUID settingsId, Long parserResultId);
}

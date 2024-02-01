package parser.app.webscraper.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.ParserResultDTO;
import parser.userService.openapi.model.ParsingPresetDTO;

import java.util.List;

public interface ParserService {
    ResponseEntity<Void> createParserSettings(Long userId, ParsingPresetDTO parsingPresetDTO);

    ParsingPresetDTO findParserSettingsById(String storageId, String settingsId);

    ResponseEntity<Void> updateParserSettingsById(String storageId, String settingsId, ParsingPresetDTO parsingPresetDTO);

    ResponseEntity<Void> deleteParserSettingsById(String storageId, String settingsId);

    List<ParsingPresetDTO> getAllParserSettingsByUserId(Long userId);


    ResponseEntity<Void> runParser(String storageId, String settingsId, ParserResultDTO parserResultDTO);

    ResponseEntity<Resource> downloadFile(String storageId, String settingsId, Long parserResultId);
}

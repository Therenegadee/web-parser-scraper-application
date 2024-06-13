package parser.app.webscraper.services.interfaces;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.ParsingResultDTO;
import parser.userService.openapi.model.ParsingPresetDTO;

public interface ParserService {

    ResponseEntity<Void> runParser(ParsingPresetDTO parsingPresetDTO, ParsingResultDTO parserResultDTO);

    ResponseEntity<Resource> downloadFile(ObjectId storageId, ObjectId presetId, Long parserResultId);

    ResponseEntity<Void> createParserSettings(Long userId, ParsingPresetDTO parsingPresetDTO);

    ParsingPresetDTO findParserSettingsById(ObjectId storageId, ObjectId presetId);

    ResponseEntity<Void> deleteParserSettingsById(ObjectId storageId, ObjectId folderId, ObjectId presetId);
}

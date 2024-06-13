package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.app.webscraper.services.interfaces.ParserService;
import parser.userService.openapi.api.ParserApiDelegate;
import parser.userService.openapi.model.ParsingPresetDTO;
import parser.userService.openapi.model.RunParserRequest;

@RestController
@RequestMapping("/api/parser")
@RequiredArgsConstructor
public class ParserController implements ParserApiDelegate {
    private final ParserService parserService;

    @Observed
    @Override
    @PostMapping("/preset")
    public ResponseEntity<Void> createParserSettings(
            @RequestParam(name = "userId") Long userId,
            @RequestBody ParsingPresetDTO parsingPresetDTO
    ) {
        return parserService.createParserSettings(userId, parsingPresetDTO);
    }

    @Observed
    @Override
    @GetMapping("/preset/{presetId}")
    public ResponseEntity<ParsingPresetDTO> getParserSettingsById(
            @PathVariable("presetId") @Valid String presetId,
            @RequestParam(name = "storageId") String storageId
    ) {
        return ResponseEntity.ok(parserService.findParserSettingsById(new ObjectId(storageId), new ObjectId(presetId)));
    }

    @Observed
    @Override
    @DeleteMapping("/preset/{presetId}")
    public ResponseEntity<Void> deleteParserSettingsById(
            @PathVariable("presetId") @Valid String presetId,
            @RequestParam("folderId") String folderId,
            @RequestParam(name = "storageId") String storageId
    ) {
        return parserService.deleteParserSettingsById(new ObjectId(storageId), new ObjectId(folderId), new ObjectId(presetId));
    }

    @Observed
    @Override
    @PostMapping("/preset/{presetId}")
    public ResponseEntity<Void> runParser(@PathVariable("presetId") String presetId, @RequestBody RunParserRequest request) {
        return parserService.runParser(request.getParsingPreset(), request.getParsingResult());
    }

    @Observed
    @Override
    @GetMapping("/preset/{presetId}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("presetId") @Valid String presetId,
            @RequestParam("storageId") @Valid String storageId,
            @RequestParam("resultId") @Valid Long resultId
    ) {
        return parserService.downloadFile(new ObjectId(storageId), new ObjectId(presetId), resultId);
    }
}


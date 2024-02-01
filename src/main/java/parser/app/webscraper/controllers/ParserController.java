package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.app.webscraper.services.interfaces.ParserService;
import parser.userService.openapi.api.ParserApiDelegate;
import parser.userService.openapi.model.ParserResultDTO;
import parser.userService.openapi.model.ParsingPresetDTO;

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
        return ResponseEntity.ok(parserService.findParserSettingsById(storageId, presetId));
    }

    @Observed
    @Override
    @DeleteMapping("/preset/{presetId}")
    public ResponseEntity<Void> deleteParserSettingsById(
            @PathVariable("presetId") @Valid String presetId,
            @RequestParam(name = "storageId") String storageId
    ) {
        return parserService.deleteParserSettingsById(storageId, presetId);
    }

    @Observed
    @Override
    @PostMapping("/preset/{presetId}")
    public ResponseEntity<Void> runParser(
            @PathVariable("presetId") @Valid String presetId,
            @RequestParam("storageId") String storageId,
            @RequestBody ParserResultDTO parserResultDTO
    ) {
        return parserService.runParser(storageId, presetId, parserResultDTO);
    }

    @Observed
    @Override
    @GetMapping("/preset/{presetId}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("presetId") @Valid String presetId,
            @RequestParam("storageId") @Valid String storageId,
            @RequestParam("resultId") @Valid Long resultId
    ) {
        return parserService.downloadFile(storageId, presetId, resultId);
    }


}


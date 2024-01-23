package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.app.webscraper.services.interfaces.ParserService;
import parser.userService.openapi.api.ParserApiDelegate;
import parser.userService.openapi.model.ParserResultOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.UUID;

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
            @RequestBody UserParserSettingsOpenApi userParserSettingsOpenApi,
            @RequestParam(name = "folderName", required = false) String folderName
    ) {
        if(!folderName.isEmpty()) {
            return parserService.createParserSettings(userId, folderName, userParserSettingsOpenApi);
        }
        return parserService.createParserSettings(userId, userParserSettingsOpenApi);
    }

    @Observed
    @Override
    @GetMapping("/preset/{presetId}")
    public ResponseEntity<UserParserSettingsOpenApi> getParserSettingsById(
            @PathVariable("presetId") @Valid UUID id,
            @RequestParam(name = "storageId") UUID storageId
    ) {
        return ResponseEntity.ok(parserService.findParserSettingsById(storageId, id));
    }

    @Observed
    @Override
    @DeleteMapping("/preset/{presetId}")
    public ResponseEntity<Void> deleteParserSettingsById(
            @PathVariable("presetId") @Valid UUID id,
            @RequestParam(name = "storageId") UUID storageId
    ) {
        return parserService.deleteParserSettingsById(storageId, id);
    }

    @Observed
    @Override
    @PostMapping("/preset/{presetId}")
    public ResponseEntity<Void> runParser(
            @PathVariable("presetId") @Valid UUID id,
            @RequestParam("storageId") UUID storageId,
            @RequestBody ParserResultOpenApi parserResultOpenApi
    ) {
        return parserService.runParser(storageId, id, parserResultOpenApi);
    }

    @Observed
    @Override
    @GetMapping("/preset/{presetId}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("presetId") @Valid UUID settingsId,
            @RequestParam("storageId") @Valid UUID storageId,
            @RequestParam("resultId") @Valid Long resultId
    ) {
        return parserService.downloadFile(storageId, settingsId, resultId);
    }


}


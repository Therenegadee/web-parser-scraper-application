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

import java.util.List;

@RestController
@RequestMapping("/api/parser")
@RequiredArgsConstructor
public class ParserController implements ParserApiDelegate {
    private final ParserService parserService;

    @Observed
    @Override
    @GetMapping("/preset")
    //    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserParserSettingsOpenApi>> getParserSettingsByUserId(
            @RequestParam(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(parserService.getAllParserSettingsByUserId(userId));
    }

    @Observed
    @Override
    @PostMapping("/preset")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> createParserSettings(
            @RequestParam(name = "userId") Long userId,
            @RequestBody UserParserSettingsOpenApi userParserSettingsOpenApi
    ) {
        return parserService.createParserSettings(userId, userParserSettingsOpenApi);
    }

    @Observed
    @Override
    @GetMapping("/preset/{id}")
    //    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserParserSettingsOpenApi> getParserSettingsById(
            @PathVariable("id") @Valid Long id
    ) {
        return ResponseEntity.ok(parserService.getParserSettingsById(id));
    }

    @Observed
    @Override
    @DeleteMapping("/preset/{id}")
    //    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteParserSettingsById(
            @PathVariable("id") @Valid Long id
    ) {
        return parserService.deleteParserSettingsById(id);
    }

    @Observed
    @Override
    @PostMapping("/preset/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> runParser(
            @PathVariable("id") @Valid Long id,
            @RequestBody ParserResultOpenApi parserResultOpenApi
    ) {
        return parserService.runParser(id, parserResultOpenApi);
    }

    @Observed
    @Override
    @GetMapping("/preset/{id}/download")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("id") @Valid Long id
    ) {
        return parserService.downloadFile(id);
    }
}


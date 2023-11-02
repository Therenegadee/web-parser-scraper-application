package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
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

    @Override
    @Observed
    public ResponseEntity<List<ParserResultOpenApi>> getAllParserQueries() {
        return ResponseEntity
                .ok(parserService.getAllParserQueries().stream().toList());

    }

    @Override
    @Observed
    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParserResultOpenApi> showParserResultsById(@PathVariable("id") @Valid Long id) {
        return ResponseEntity.ok(parserService.showParserResultsById(id));
    }

    @Override
    @Observed
    @PostMapping("/settings")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> setParserSettings(@RequestBody UserParserSettingsOpenApi userParserSettingsOpenApi) {
        return parserService.setParserSettings(userParserSettingsOpenApi);
    }

    @Override
    @Observed
    @PostMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> runParser(@PathVariable("id") @Valid Long id) {
        return parserService.runParser(id);
    }

    @Override
    @Observed
    @GetMapping("/{id}/download")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") @Valid Long id) {
        return parserService.downloadFile(id);
    }
}


package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.app.webscraper.services.interfaces.FolderService;
import parser.userService.openapi.api.FolderApiDelegate;
import parser.userService.openapi.model.FolderItemOpenApi;
import parser.userService.openapi.model.FolderOpenApi;

import java.util.List;

@RestController
@RequestMapping("/api/folder")
@RequiredArgsConstructor
public class FoldersController implements FolderApiDelegate {
    private final FolderService folderService;

    @Observed
    @GetMapping
    @Override
    public ResponseEntity<List<FolderItemOpenApi>> getAllFolderItemsByUserId(
            @RequestParam(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(folderService.getAllFolderItemsByUserId(userId));
    }

    @Observed
    @PostMapping
    @Override
    public ResponseEntity<Void> createFolder(
            @RequestParam(name = "userId") @Valid Long userId,
            @RequestBody FolderOpenApi folderOpenApi
    ) {
        return folderService.createNewFolder(userId, folderOpenApi);
    }


    @Observed
    @GetMapping("/{folderId}")
    @Override
    public ResponseEntity<List<FolderItemOpenApi>> getAllFolderItemsByFolderId(
            @PathVariable(name = "folderId") @Valid Long folderId
    ) {
        return ResponseEntity.ok(folderService.getAllFolderItemsByFolderId(folderId));
    }

    @Observed
    @PutMapping("/{folderId}")
    @Override
    public ResponseEntity<Void> updateFolderById(
            @PathVariable(name = "folderId") @Valid Long folderId,
            @RequestBody FolderOpenApi folderOpenApi
    ) {
        return folderService.updateFolderById(folderId, folderOpenApi);
    }

    @Observed
    @DeleteMapping("/{folderId}")
    @Override
    public ResponseEntity<Void> deleteFolderById(
            @PathVariable(name = "folderId") @Valid Long folderId
    ) {
        return folderService.deleteFolderById(folderId);
    }

}

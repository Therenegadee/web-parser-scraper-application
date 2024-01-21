package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.api.StorageApiDelegate;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;


@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController implements StorageApiDelegate {
    private final StorageService storageService;

    @Observed
    @GetMapping
    @Override
    public ResponseEntity<StorageOpenApi> getStorageByUserId (
            @RequestParam(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(storageService.findByUserId(userId));
    }

    @Observed
    @PostMapping("/folder/new")
    @Override
    public ResponseEntity<Void> createFolder(
            @RequestParam(name = "userId") @Valid Long userId,
            @RequestBody FolderOpenApi folderOpenApi
    ) {
        return storageService.createNewFolder(userId, folderOpenApi);
    }


    @Observed
    @GetMapping("/folder/{folderId}")
    @Override
    public ResponseEntity<FolderOpenApi> getFolderByFolderId(
            @PathVariable(name = "folderId") @Valid Long folderId
    ) {
        return ResponseEntity.ok(storageService.getFolderByFolderId(folderId));
    }

    @Observed
    @PutMapping("/folder/{folderId}")
    @Override
    public ResponseEntity<Void> updateFolderById(
            @PathVariable(name = "folderId") @Valid Long folderId,
            @RequestBody FolderOpenApi folderOpenApi
    ) {
        return storageService.updateFolderById(folderId, folderOpenApi);
    }

    @Observed
    @DeleteMapping("/folder/{folderId}")
    @Override
    public ResponseEntity<Void> deleteFolderById(
            @PathVariable(name = "folderId") @Valid Long folderId
    ) {
        return storageService.deleteFolderById(folderId);
    }

}

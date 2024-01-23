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

import java.util.UUID;


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
    @PutMapping
    @Override
    public ResponseEntity<Void> updateStorageByUserId (
            @RequestParam(name = "userId") Long userId,
            @RequestBody StorageOpenApi storageOpenApi
    ) {
        return storageService.updateStorageByUserId(userId, storageOpenApi);
    }

    @Observed
    @PutMapping("/{storageId}")
    @Override
    public ResponseEntity<Void> updateStorageById (
            @PathVariable(name = "storageId") UUID storageId,
            @RequestBody StorageOpenApi storageOpenApi
    ) {
        return storageService.updateStorageById(storageId, storageOpenApi);
    }



    @Observed
    @GetMapping("/{storageId}/folder/{folderId}")
    @Override
    public ResponseEntity<FolderOpenApi> getFolderByFolderId(
            @PathVariable(name = "storageId") @Valid UUID storageId,
            @PathVariable(name = "folderId") @Valid UUID folderId
    ) {
        return ResponseEntity.ok(storageService.findFolderById(storageId, folderId));
    }

    @Observed
    @PutMapping("/{storageId}/folder/{folderId}")
    @Override
    public ResponseEntity<Void> updateFolderById(
            @PathVariable(name = "storageId") @Valid UUID storageId,
            @PathVariable(name = "folderId") @Valid UUID folderId,
            @RequestBody FolderOpenApi folderOpenApi
    ) {
        return storageService.updateFolderById(storageId, folderId, folderOpenApi);
    }

    @Observed
    @DeleteMapping("/{storageId}/folder/{folderId}")
    @Override
    public ResponseEntity<Void> deleteFolderById(
            @PathVariable(name = "storageId") @Valid UUID storageId,
            @PathVariable(name = "folderId") @Valid UUID folderId
    ) {
        return storageService.deleteFolderById(storageId, folderId);
    }

}

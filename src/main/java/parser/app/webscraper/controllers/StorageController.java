package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.app.webscraper.services.interfaces.FolderService;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.api.StorageApiDelegate;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;


@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController implements StorageApiDelegate {
    private final StorageService storageService;
    private final FolderService folderService;

    @Observed
    @PostMapping
    @Override
    public ResponseEntity<Void> createStorage(@RequestParam(name = "userId") Long userId) {
        return storageService.createStorage(userId);
    }

    @Observed
    @GetMapping("/{storageId}")
    @Override
    public ResponseEntity<StorageOpenApi> getStorageById(@PathVariable String storageId) {
        return ResponseEntity.ok(storageService.findByStorageId(storageId));
    }

    @Observed
    @GetMapping
    @Override
    public ResponseEntity<StorageOpenApi> getStorageByUserId(
            @RequestParam(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(storageService.findByUserId(userId));
    }


    @Observed
    @PutMapping
    @Override
    public ResponseEntity<Void> updateStorageByUserId(
            @RequestParam(name = "userId") Long userId,
            @RequestBody StorageOpenApi storageOpenApi
    ) {
        return storageService.updateStorageByUserId(userId, storageOpenApi);
    }

    @Observed
    @PutMapping("/{storageId}")
    @Override
    public ResponseEntity<Void> updateStorageById(
            @PathVariable(name = "storageId") String storageId,
            @RequestBody StorageOpenApi storageOpenApi
    ) {
        return storageService.updateStorageById(storageId, storageOpenApi);
    }

    @Observed
    @PostMapping("/folder")
    @Override
    public ResponseEntity<Void> createFolder(
            @RequestParam(name = "userId") Long userId,
            @RequestBody FolderOpenApi folder
    ) {
        return folderService.createFolder(userId, folder);
    }


    @Observed
    @GetMapping("/{storageId}/folder/{folderId}")
    @Override
    public ResponseEntity<FolderOpenApi> getFolderByFolderId(
            @PathVariable(name = "storageId") @Valid String storageId,
            @PathVariable(name = "folderId") @Valid String folderId
    ) {
        return ResponseEntity.ok(folderService.findFolderById(storageId, folderId));
    }

    @Observed
    @PutMapping("/{storageId}/folder/{folderId}")
    @Override
    public ResponseEntity<Void> updateFolderById(
            @PathVariable(name = "storageId") @Valid String storageId,
            @PathVariable(name = "folderId") @Valid String folderId,
            @RequestBody FolderOpenApi folderOpenApi
    ) {
        return folderService.updateFolderById(storageId, folderId, folderOpenApi);
    }

    @Observed
    @DeleteMapping("/{storageId}/folder/{folderId}")
    @Override
    public ResponseEntity<Void> deleteFolderById(
            @PathVariable(name = "storageId") @Valid String storageId,
            @PathVariable(name = "folderId") @Valid String folderId
    ) {
        return folderService.deleteFolderById(storageId, folderId);
    }


}

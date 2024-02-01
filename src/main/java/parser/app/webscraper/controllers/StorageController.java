package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.app.webscraper.services.interfaces.FolderService;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.api.StorageApiDelegate;
import parser.userService.openapi.model.FolderDTO;
import parser.userService.openapi.model.StorageDTO;


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
    public ResponseEntity<StorageDTO> getStorageById(@PathVariable String storageId) {
        return ResponseEntity.ok(storageService.findByStorageId(storageId));
    }

    @Observed
    @GetMapping
    @Override
    public ResponseEntity<StorageDTO> getStorageByUserId(
            @RequestParam(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(storageService.findByUserId(userId));
    }


    @Observed
    @PutMapping
    @Override
    public ResponseEntity<Void> updateStorageByUserId(
            @RequestParam(name = "userId") Long userId,
            @RequestBody StorageDTO storageDTO
    ) {
        return storageService.updateStorageByUserId(userId, storageDTO);
    }

    @Observed
    @PutMapping("/{storageId}")
    @Override
    public ResponseEntity<Void> updateStorageById(
            @PathVariable(name = "storageId") String storageId,
            @RequestBody StorageDTO storageDTO
    ) {
        return storageService.updateStorageById(storageId, storageDTO);
    }

    @Observed
    @PostMapping("/folder")
    @Override
    public ResponseEntity<Void> createFolder(
            @RequestParam(name = "userId") Long userId,
            @RequestBody FolderDTO folder
    ) {
        return folderService.createFolder(userId, folder);
    }


    @Observed
    @GetMapping("/{storageId}/folder/{folderId}")
    @Override
    public ResponseEntity<FolderDTO> getFolderByFolderId(
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
            @RequestBody FolderDTO folderDTO
    ) {
        return folderService.updateFolderById(storageId, folderId, folderDTO);
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

package parser.app.webscraper.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;

import java.util.UUID;

public interface StorageService {

    ResponseEntity<Void> createStorage(Long userId);

    StorageOpenApi findByStorageId(UUID storageId);

    StorageOpenApi findByUserId(Long userId);

    ResponseEntity<Void> updateStorageById(UUID storageId, StorageOpenApi storageOpenApi);

    ResponseEntity<Void> updateStorageByUserId(Long userId, StorageOpenApi storageOpenApi);

    ResponseEntity<Void> createFolder(UUID storageId, FolderOpenApi folderOpenApi);

    FolderOpenApi findFolderById(UUID storageId, UUID folderId);

    ResponseEntity<Void> updateFolderById(UUID storageId, UUID storageItemId, FolderOpenApi folderOpenApi);

    ResponseEntity<Void> deleteFolderById(UUID storageId, UUID folderId);

}

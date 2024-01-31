package parser.app.webscraper.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;


public interface StorageService {

    ResponseEntity<Void> createStorage(Long userId);

    StorageOpenApi findByStorageId(String storageId);

    StorageOpenApi findByUserId(Long userId);

    ResponseEntity<Void> updateStorageById(String storageId, StorageOpenApi storageOpenApi);

    ResponseEntity<Void> updateStorageByUserId(Long userId, StorageOpenApi storageOpenApi);

//    ResponseEntity<Void> createFolder(Long userId, FolderOpenApi folderOpenApi);
//
//    FolderOpenApi findFolderById(String storageId, String folderId);
//
//    ResponseEntity<Void> updateFolderById(String storageId, String folderId, FolderOpenApi folderOpenApi);
//
//    ResponseEntity<Void> deleteFolderById(String storageId, String folderId);

}

package parser.app.webscraper.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageItemOpenApi;
import parser.userService.openapi.model.StorageOpenApi;

import java.util.List;

public interface StorageService {
    StorageOpenApi getStorageByUserId(Long userId);

    FolderOpenApi getFolderByFolderId(Long folderId);

    ResponseEntity<Void> createNewFolder(Long userId, FolderOpenApi folderOpenApi);

    ResponseEntity<Void> deleteFolderById(Long folderId);

    ResponseEntity<Void> updateFolderById(Long folderId, FolderOpenApi folderOpenApi);
}

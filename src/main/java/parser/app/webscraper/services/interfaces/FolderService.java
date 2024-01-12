package parser.app.webscraper.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.FolderItemOpenApi;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageItemOpenApi;

import java.util.List;

public interface FolderService {
    List<StorageItemOpenApi> getAllFolderItemsByUserId(Long userId);

    List<StorageItemOpenApi> getAllFolderItemsByFolderId(Long folderId);

    ResponseEntity<Void> createNewFolder(Long userId, FolderOpenApi folderOpenApi);

    ResponseEntity<Void> deleteFolderById(Long folderId);

    ResponseEntity<Void> updateFolderById(Long folderId, FolderOpenApi folderOpenApi);
}

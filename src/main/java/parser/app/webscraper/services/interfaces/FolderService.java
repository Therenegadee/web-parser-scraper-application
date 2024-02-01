package parser.app.webscraper.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.FolderDTO;

public interface FolderService {
    ResponseEntity<Void> createFolder(Long userId, FolderDTO folderDTO);
    FolderDTO findFolderById(String storageId, String folderId);
    ResponseEntity<Void> updateFolderById(String storageId, String folderId, FolderDTO folderDTO);
    ResponseEntity<Void> deleteFolderById(String storageId, String folderId);
}

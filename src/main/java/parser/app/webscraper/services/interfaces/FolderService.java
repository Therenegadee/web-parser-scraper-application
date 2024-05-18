package parser.app.webscraper.services.interfaces;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.FolderDTO;

public interface FolderService {
    ResponseEntity<Void> createFolder(Long userId, FolderDTO folderDTO);
    FolderDTO findFolderById(ObjectId storageId, ObjectId folderId);
    ResponseEntity<Void> updateFolderById(ObjectId storageId, ObjectId folderId, FolderDTO folderDTO);
    ResponseEntity<Void> deleteFolderById(ObjectId storageId, ObjectId folderId);
}

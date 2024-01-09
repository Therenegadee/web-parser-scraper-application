package parser.app.webscraper.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.app.webscraper.models.FolderItem;
import parser.userService.openapi.model.FolderItemOpenApi;
import parser.userService.openapi.model.FolderOpenApi;

import java.util.List;

public interface FolderService {
    List<FolderItemOpenApi> getAllFolderItemsByUserId(Long userId);

    List<FolderItemOpenApi> getAllFolderItemsByFolderId(Long folderId);

    ResponseEntity<Void> createNewFolder(Long userId, FolderOpenApi folderOpenApi);

    ResponseEntity<Void> deleteFolderById(Long folderId);

    ResponseEntity<Void> updateFolderById(Long folderId, FolderOpenApi folderOpenApi);
}

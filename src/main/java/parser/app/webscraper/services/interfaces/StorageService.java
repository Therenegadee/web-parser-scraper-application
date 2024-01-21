package parser.app.webscraper.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.UserParserSetting;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.UUID;

public interface StorageService {

    StorageOpenApi findByStorageId(UUID storageId);

    StorageOpenApi findByUserId(Long userId);

    ResponseEntity<Void> updateStorageById(UUID storageId, StorageOpenApi storageOpenApi);

    ResponseEntity<Void> updateStorageByUserId(Long userId, StorageOpenApi storageOpenApi);

    FolderOpenApi findFolderById(UUID storageId, UUID folderId);

    UserParserSettingsOpenApi findParserSettingsById(UUID storageId, UUID settingsId);

    ResponseEntity<Void> updateFolderById(UUID storageId, UUID storageItemId, FolderOpenApi folderOpenApi);

    ResponseEntity<Void> updateSettingsById(UUID storageId, UUID storageItemId, UserParserSettingsOpenApi userParserSettingsOpenApi);

    ResponseEntity<Void> deleteStorageItemById(UUID storageId, UUID storageItemId);
}

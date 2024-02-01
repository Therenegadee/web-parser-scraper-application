package parser.app.webscraper.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.FolderDTO;
import parser.userService.openapi.model.StorageDTO;


public interface StorageService {

    ResponseEntity<Void> createStorage(Long userId);

    StorageDTO findByStorageId(String storageId);

    StorageDTO findByUserId(Long userId);

    ResponseEntity<Void> updateStorageById(String storageId, StorageDTO storageDTO);

    ResponseEntity<Void> updateStorageByUserId(Long userId, StorageDTO storageDTO);

}

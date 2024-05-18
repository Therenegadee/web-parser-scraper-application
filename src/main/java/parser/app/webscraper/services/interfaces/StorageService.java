package parser.app.webscraper.services.interfaces;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import parser.app.webscraper.models.Storage;
import parser.userService.openapi.model.StorageDTO;


public interface StorageService {

    ResponseEntity<Void> createStorage(Long userId);

    Storage findByStorageId(ObjectId storageId);

    Storage findByUserId(Long userId);

    Storage save(Storage storage);

    ResponseEntity<Void> updateStorageById(String storageId, StorageDTO storageDTO);

    ResponseEntity<Void> updateStorageByUserId(Long userId, StorageDTO storageDTO);

}

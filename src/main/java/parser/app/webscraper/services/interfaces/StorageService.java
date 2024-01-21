package parser.app.webscraper.services.interfaces;

import parser.userService.openapi.model.StorageOpenApi;

public interface StorageService {

    StorageOpenApi findByStorageId(Long storageId);
    StorageOpenApi findByUserId(Long userId);
    void updateStorageById(Long storageId, StorageOpenApi storageOpenApi);

    void updateStorageByUserId(Long userId, StorageOpenApi storageOpenApi);


}

package parser.app.webscraper.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.FolderMapper;
import parser.app.webscraper.mappers.openapi.StorageMapper;
import parser.app.webscraper.mappers.openapi.UserParserSettingsMapper;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.models.UserParserSetting;
import parser.app.webscraper.repository.StorageRepository;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;
    private final FolderMapper folderMapper;
    private final UserParserSettingsMapper userParserSettingsMapper;

    @Observed
    @Override
    public StorageOpenApi findByStorageId(UUID storageId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %d wasn't found", storageId)));
        return storageMapper.toOpenApi(storage);
    }

    @Observed
    @Override
    public StorageOpenApi findByUserId(Long userId) {
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with user id %d wasn't found", userId)));
        return storageMapper.toOpenApi(storage);
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateStorageById(UUID storageId, StorageOpenApi storageOpenApi) {
        Storage storage = storageMapper.toStorage(storageOpenApi);
        storageRepository.updateByStorageId(storageId, storage);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateStorageByUserId(Long userId, StorageOpenApi storageOpenApi) {
        Storage storage = storageMapper.toStorage(storageOpenApi);
        storageRepository.updateByUserId(userId, storage);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public FolderOpenApi findFolderById(UUID storageId, UUID folderId) {
        return folderMapper.toOpenApi(
                (Folder) storageRepository
                        .findStorageItemById(storageId, folderId)
                        .orElseThrow(() -> new NotFoundException(String.format("Folder with id %d in storage (id: %d) wasn't found", folderId, storageId)))
        );
    }

    @Observed
    @Override
    public UserParserSettingsOpenApi findParserSettingsById(UUID storageId, UUID settingsId) {
        return userParserSettingsMapper.toOpenApi(
                (UserParserSetting) storageRepository
                        .findStorageItemById(storageId, settingsId)
                        .orElseThrow(() -> new NotFoundException(String.format("UserParserSetting with id %d in Storage (id: %d) wasn't found", settingsId, storageId)))
        );
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateFolderById(
            UUID storageId,
            UUID storageItemId,
            FolderOpenApi folderOpenApi
    ) {
        Folder folder = folderMapper.toFolder(folderOpenApi);
        storageRepository.updateStorageItemById(storageId, storageItemId, folder);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateSettingsById(
            UUID storageId,
            UUID storageItemId,
            UserParserSettingsOpenApi userParserSettingsOpenApi
    ) {
        UserParserSetting userParserSetting = userParserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        storageRepository.updateStorageItemById(storageId, storageItemId, userParserSetting);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> deleteStorageItemById(UUID storageId, UUID storageItemId) {
        storageRepository.deleteStorageItemById(storageId, storageItemId);
        return ResponseEntity
                .status(204)
                .build();
    }
}

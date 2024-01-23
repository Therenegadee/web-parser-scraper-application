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
import parser.app.webscraper.models.StorageItem;
import parser.app.webscraper.repository.StorageRepository;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;
    private final FolderMapper folderMapper;

    @Observed
    @Override
    public StorageOpenApi findByStorageId(UUID storageId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        return storageMapper.toOpenApi(storage);
    }

    @Observed
    @Override
    public StorageOpenApi findByUserId(Long userId) {
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with user id %s wasn't found", userId)));
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
                storageRepository
                        .findFolderById(storageId, folderId)
                        .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)))
        );
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateFolderById(
            UUID storageId,
            UUID storageItemId,
            FolderOpenApi folderOpenApi
    ) {
        Folder sourceFolder = folderMapper.toFolder(folderOpenApi);
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        List<StorageItem> storageItems = storage.getStorageItems();

        Folder targetFolder = storageRepository
                .findFolderById(storageItems, storageItemId)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", storageItemId, storageId)));

        if (!targetFolder.getParentFolderId().equals(sourceFolder.getParentFolderId())) {
            Folder targetParentFolder = storageRepository
                    .findFolderById(storageItems, targetFolder.getParentFolderId())
                    .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", storageItemId, storageId)));
            targetParentFolder.getStorageItems().remove(targetFolder);
            if (Objects.isNull(sourceFolder.getParentFolderId())) {
                storageItems.add(targetFolder);
            } else {
                Folder sourceParentFolder = storageRepository
                        .findFolderById(storageItems, sourceFolder.getParentFolderId())
                        .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", storageItemId, storageId)));
                sourceParentFolder.getStorageItems().add(targetFolder);
            }
        }

        targetFolder.setName(sourceFolder.getName());
        targetFolder.setStorageItems(sourceFolder.getStorageItems());
        targetFolder.setTags(sourceFolder.getTags());
        targetFolder.setParentFolderId(sourceFolder.getParentFolderId());
        storageRepository.updateByStorageId(storageId, storage);

        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> deleteFolderById(UUID storageId, UUID folderId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        List<StorageItem> storageItems = storage.getStorageItems();

        Folder folder = storageRepository
                .findFolderById(storageItems, folderId)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)));
        Folder parentFolder = storageRepository
                .findFolderById(storageItems, folder.getParentFolderId())
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folder.getParentFolderId(), storageId)));

        List<StorageItem> folderItems = folder.getStorageItems();
        parentFolder.getStorageItems().remove(folder);
        storage.addStorageItems(folderItems);

        storageRepository.updateByStorageId(storageId, storage);

        return ResponseEntity
                .status(204)
                .build();
    }


}

package parser.app.webscraper.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parser.app.webscraper.exceptions.BadRequestException;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.FolderMapper;
import parser.app.webscraper.mappers.openapi.StorageMapper;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.models.StorageItem;
import parser.app.webscraper.repository.StorageRepository;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;
    private final FolderMapper folderMapper;

    @Observed
    @Override
    public ResponseEntity<Void> createStorage(Long userId) {
        Optional<Storage> storageOptional = storageRepository.findByUserId(userId);
        if (storageOptional.isPresent()) {
            throw new BadRequestException(String.format("Storage for User With id %d is already exists!", userId));
        } else {
            Storage storage = new Storage();
            storage.setUserId(userId);
            storageRepository.save(storage);
            return ResponseEntity
                    .status(201)
                    .build();
        }
    }

    @Observed
    @Override
    public StorageOpenApi findByStorageId(String storageId) {
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
    public ResponseEntity<Void> updateStorageById(String storageId, StorageOpenApi storageOpenApi) {
        Storage storageDTO = storageMapper.toStorage(storageOpenApi);
        storageDTO.setId(storageId);
        storageRepository.save(storageDTO);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateStorageByUserId(Long userId, StorageOpenApi storageOpenApi) {
        Storage storageDTO = storageMapper.toStorage(storageOpenApi);
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage for user with id %d wasn't found", userId)));
        storageDTO.setId(storage.getId());
        storageRepository.save(storage);
        return ResponseEntity
                .ok()
                .build();
    }

//    @Observed
//    @Override
//    public ResponseEntity<Void> createFolder(Long userId, FolderOpenApi folderOpenApi) {
//        Folder folder = folderMapper.toFolder(folderOpenApi);
//        Storage storage = storageRepository
//                .findByUserId(userId)
//                .orElseThrow(() -> new NotFoundException(String.format(String.format("Storage for user with id %s wasn't found", userId))));
//        String parentId = folder.getParentFolderId();
//        if (Objects.isNull(parentId)) {
//            storage.addStorageItem(folder);
//        } else {
//            storage.addStorageItemInsideFolder(folder, parentId);
//        }
//        folder.setStorageId(storage.getId());
//        storageRepository.save(storage);
//        return ResponseEntity
//                .status(201)
//                .build();
//    }

//    @Observed
//    @Override
//    public FolderOpenApi findFolderById(String storageId, String folderId) {
//        Storage storage = storageRepository
//                .findById(storageId)
//                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
//        Folder folder = storage
//                .findFolderById(storage.getStorageItems(), folderId)
//                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s wasn't found", folderId)));
//        return folderMapper.toOpenApi(folder);
//    }

//    @Observed
//    @Override
//    public ResponseEntity<Void> updateFolderById(
//            String storageId,
//            String folderId,
//            FolderOpenApi folderOpenApi
//    ) {
//        Folder sourceFolder = folderMapper.toFolder(folderOpenApi);
//        Storage storage = storageRepository
//                .findById(storageId)
//                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
//
//        Folder targetFolder = storage.findFolderById(storage.getStorageItems(), folderId)
//                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)));
//
//        if (!targetFolder.getParentFolderId().equals(sourceFolder.getParentFolderId())) {
//            Folder targetParentFolder = storage.findFolderById(storage.getStorageItems(), targetFolder.getParentFolderId())
//                    .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)));
//            targetParentFolder.getStorageItems().remove(targetFolder);
//            if (Objects.isNull(sourceFolder.getParentFolderId())) {
//                storage.addStorageItem(targetFolder);
//            } else {
//                Folder sourceParentFolder = storage
//                        .findFolderById(storage.getStorageItems(), sourceFolder.getParentFolderId())
//                        .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)));
//                sourceParentFolder.addStorageItem(targetFolder);
//            }
//        }
//
//        targetFolder.setName(sourceFolder.getName());
//        targetFolder.setStorageItems(sourceFolder.getStorageItems());
//        targetFolder.setTags(sourceFolder.getTags());
//        targetFolder.setParentFolderId(sourceFolder.getParentFolderId());
//        storageRepository.save(storage);
//
//        return ResponseEntity
//                .ok()
//                .build();
//    }
//
//    @Observed
//    @Override
//    public ResponseEntity<Void> deleteFolderById(String storageId, String folderId) {
//        Storage storage = storageRepository
//                .findById(storageId)
//                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
//
//        Folder folder = storage.findFolderById(storage.getStorageItems(), folderId)
//                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)));
//        Folder parentFolder = storage.findFolderById(storage.getStorageItems(), folder.getParentFolderId())
//                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folder.getParentFolderId(), storageId)));
//
//        List<StorageItem> folderItems = folder.getStorageItems();
//        parentFolder.getStorageItems().remove(folder);
//        storage.addStorageItems(folderItems);
//
//        storageRepository.save(storage);
//
//        return ResponseEntity
//                .status(204)
//                .build();
//    }


}

package parser.app.webscraper.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.DAO.StorageMongoTemplate;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.FolderMapper;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.services.interfaces.FolderService;
import parser.userService.openapi.model.FolderDTO;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderMapper folderMapper;
    private final StorageMongoTemplate storageRepository;

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Void> createFolder(Long userId, FolderDTO folderDTO) {
        Folder folder = folderMapper.toFolder(folderDTO);
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format(String.format("Storage for user with id %s wasn't found", userId))));
        ObjectId parentId = folder.getParentFolderId();
        if (Objects.isNull(parentId)) {
            storage.addStorageItem(folder);
        } else {
            storage.addStorageItemInsideFolder(folder, parentId);
        }
        folder.setStorageId(storage.getId());
        storageRepository.save(storage);
        return ResponseEntity
                .status(201)
                .build();
    }

    @Observed
    @Transactional
    @Override
    public FolderDTO findFolderById(ObjectId storageId, ObjectId folderId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        Folder folder = storage
                .findFolderById(folderId)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s wasn't found", folderId)));
        return folderMapper.toDTO(folder);
    }

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Void> updateFolderById(ObjectId storageId, ObjectId folderId, FolderDTO folderDTO) {
        Folder sourceFolder = folderMapper.toFolder(folderDTO);
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));

        Folder targetFolder = storage.findFolderById(folderId)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)));

        if (!Objects.equals(targetFolder.getParentFolderId(), sourceFolder.getParentFolderId())) {
            Folder targetParentFolder = storage.findFolderById(targetFolder.getParentFolderId())
                    .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)));
            targetParentFolder.getFolderItems().remove(targetFolder);
            if (Objects.isNull(sourceFolder.getParentFolderId())) {
                storage.addStorageItem(targetFolder);
            } else {
                Folder sourceParentFolder = storage
                        .findFolderById(sourceFolder.getParentFolderId())
                        .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", folderId, storageId)));
                sourceParentFolder.addFolderItem(targetFolder);
            }
        }

        targetFolder.setName(sourceFolder.getName());
        targetFolder.setFolderItems(sourceFolder.getFolderItems());
        targetFolder.setTags(sourceFolder.getTags());
        targetFolder.setParentFolderId(sourceFolder.getParentFolderId());
        storageRepository.save(storage);

        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Void> deleteFolderById(ObjectId storageId, ObjectId folderId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        Folder[] folderWithParentFolder = storage
                .findFolderWithParentFolder(folderId)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in Storage (id: %s) wasn't found", folderId, storageId)));
        Folder parentFolder = folderWithParentFolder[0];
        if(Objects.nonNull(parentFolder)) {
            parentFolder.getFolderItems().remove(folderWithParentFolder[1]);
        } else {
            storage.getStorageItems().remove(folderWithParentFolder[1]);
        }
        storageRepository.save(storage);
        return ResponseEntity
                .ok()
                .build();
    }
}

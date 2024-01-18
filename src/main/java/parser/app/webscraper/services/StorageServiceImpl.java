package parser.app.webscraper.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parser.app.webscraper.dao.jdbc.interfaces.FolderDao;
import parser.app.webscraper.dao.jdbc.interfaces.StorageDao;
import parser.app.webscraper.dao.jdbc.interfaces.UserParserSettingsDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.StorageItemMapper;
import parser.app.webscraper.mappers.openapi.FolderMapper;
import parser.app.webscraper.mappers.openapi.StorageMapper;
import parser.app.webscraper.mappers.openapi.UserParserSettingsMapper;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageOpenApi;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final FolderDao folderDao;
    private final StorageDao storageDao;
    private final UserParserSettingsDao userParserSettingsDao;
    private final StorageItemMapper storageItemMapper;
    private final StorageMapper storageMapper;
    private final FolderMapper folderMapper;
    private final UserParserSettingsMapper userParserSettingsMapper;

    @Observed
    @Override
    public StorageOpenApi getStorageByUserId(Long userId) {
        Storage storage = storageDao.findByUserId(userId);
        return storageMapper.toOpenApi(storage);
    }

    @Observed
    @Override
    public FolderOpenApi getFolderByFolderId(Long folderId) {
        return folderMapper
                .toOpenApi(folderDao.findByFolderId(folderId)
                        .orElseThrow(() -> new NotFoundException(String.format("Folder with idd %d wasn't found", folderId))));
    }

    @Observed
    @Override
    public ResponseEntity<Void> createNewFolder(Long userId, FolderOpenApi folderOpenApi) {
        Folder folder = folderMapper.toFolder(folderOpenApi);
        folderDao.save(folder);
        return ResponseEntity
                .status(201)
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> deleteFolderById(Long folderId) {
        folderDao.deleteById(folderId);
        return ResponseEntity
                .status(204)
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateFolderById(Long folderId, FolderOpenApi folderOpenApi) {
        Folder folder = folderMapper.toFolder(folderOpenApi);
        folderDao.updateById(folderId, folder);
        return ResponseEntity
                .ok()
                .build();
    }
}

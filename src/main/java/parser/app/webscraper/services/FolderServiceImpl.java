package parser.app.webscraper.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parser.app.webscraper.dao.interfaces.FolderDao;
import parser.app.webscraper.dao.interfaces.UserParserSettingsDao;
import parser.app.webscraper.mappers.openapi.StorageItemMapper;
import parser.app.webscraper.mappers.openapi.FolderMapper;
import parser.app.webscraper.mappers.openapi.UserParserSettingsMapper;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.StorageItem;
import parser.app.webscraper.services.interfaces.FolderService;
import parser.userService.openapi.model.FolderOpenApi;
import parser.userService.openapi.model.StorageItemOpenApi;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderDao folderDao;
    private final UserParserSettingsDao userParserSettingsDao;
    private final StorageItemMapper storageItemMapper;
    private final FolderMapper folderMapper;
    private final UserParserSettingsMapper userParserSettingsMapper;

    @Observed
    @Override
    public List<StorageItemOpenApi> getAllFolderItemsByUserId(Long userId) {
        List<StorageItem> storageItems = new ArrayList<>();
        storageItems.addAll(folderDao.findAllByUserId(userId));
        storageItems.addAll(userParserSettingsDao.findAllByUserId(userId)
                .stream()
                .filter(settings -> settings.getParentFolder() == null)
                .toList()
        );
        return storageItemMapper.toOpenApi(storageItems);
    }

    @Observed
    @Override
    public List<StorageItemOpenApi> getAllFolderItemsByFolderId(Long folderId) {
        List<StorageItem> storageItems = new ArrayList<>();
        storageItems.addAll(folderDao.findAllByParentFolderId(folderId));
        storageItems.addAll(folderDao.findAllByParentFolderId(folderId));
        return storageItemMapper.toOpenApi(storageItems);
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

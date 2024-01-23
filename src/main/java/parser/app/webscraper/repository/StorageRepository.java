package parser.app.webscraper.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.models.StorageItem;
import parser.app.webscraper.models.UserParserSetting;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Observed
public interface StorageRepository extends MongoRepository<Storage, UUID> {

    @Query("{'id' : ?0}")
    Optional<Storage> findById(UUID id);

    @Query("{'userId' : ?0}")
    Optional<Storage> findByUserId(Long userId);

    @Query("{'userId' : ?0}")
    @Update("{'$set': {'storage': ?1}}")
    void updateByUserId(Long userId, Storage storage);

    @Query("{'id' : ?0}")
    @Update("{'$set': {'storage': ?1}}")
    void updateByStorageId(UUID storageId, Storage storage);

    @Observed
    default Optional<UserParserSetting> findParserSettingsById(UUID storageId, UUID settingsId) {
        Storage storage = findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        List<StorageItem> storageItems = storage.getStorageItems();
        return findParserSettingsById(storageItems, settingsId);
    }

    @Observed
    default Optional<UserParserSetting> findParserSettingsById(List<StorageItem> storageItems, UUID id) {
        for (StorageItem item : storageItems) {
            if (item instanceof UserParserSetting && ((UserParserSetting) item).getId().equals(id)) {
                return Optional.of((UserParserSetting) item);
            } else if (item instanceof Folder) {
                Optional<UserParserSetting> result = findParserSettingsById(((Folder) item).getStorageItems(), id);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }

    default Optional<Folder> findFolderById(UUID storageId, UUID folderId) {
        Storage storage = findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        List<StorageItem> storageItems = storage.getStorageItems();
        return findFolderById(storageItems, folderId);
    }

    default Optional<Folder> findFolderById(List<StorageItem> storageItems, UUID folderId) {
        for (StorageItem item : storageItems) {
            if (item instanceof Folder) {
                if (((Folder) item).getId().equals(folderId)) {
                    return Optional.of((Folder) item);
                }
                Optional<Folder> result = findFolderById(((Folder) item).getStorageItems(), folderId);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }
}

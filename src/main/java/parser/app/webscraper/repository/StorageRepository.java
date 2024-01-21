package parser.app.webscraper.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.models.StorageItem;

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

    @Query("{'id': ?0, 'storageItems.id': ?1}")
    Optional<StorageItem> findStorageItemById(UUID storageId, UUID storageItemId);

    @Query("{'id': ?0, 'storageItems.id': ?1}")
    @Update("{'$set': {'storageItems.$': ?2}}")
    void updateStorageItemById(UUID storageId, UUID storageItemId, StorageItem storageItem);

    @Query("{'id': ?0, 'storageItems.id': ?1}")
    void deleteStorageItemById(UUID storageId, UUID storageItemId);
}

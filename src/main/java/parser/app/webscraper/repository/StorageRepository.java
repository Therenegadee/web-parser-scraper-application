package parser.app.webscraper.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.models.StorageItem;
import parser.app.webscraper.models.UserParserSetting;

import java.util.List;
import java.util.Optional;

@Observed
public interface StorageRepository extends MongoRepository<Storage, String> {

    @Query("{'id' : ?0}")
    Optional<Storage> findById(String id);

    @Query("{'userId' : ?0}")
    Optional<Storage> findByUserId(Long userId);

    @Query("{ '_id' : ?0, 'storageItems._id' : ?1 }")
    Optional<Folder> findFolderById(Long userId, String id);

    @Query("{ '_id' : ?0, 'storageItems._id' : ?1 }")
    Optional<UserParserSetting> findParserSettingsById(String storageId, String settingsId);

}

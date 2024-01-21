package parser.app.webscraper.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import parser.app.webscraper.models.Storage;

import java.util.Optional;

@Observed
public interface StorageRepository extends MongoRepository<Storage, Long> {

    @Query("{'id' : ?0}")
    Optional<Storage> findById(Long id);

    @Query("{'userId' : ?0}")
    Optional<Storage> findByUserId(Long userId);

    @Query("{'userId' : ?0}")
    @Update("{'$set': {'storage': ?1}}")
    void updateByUserId(Long userId, Storage storage);

    @Query("{'id' : ?0}")
    @Update("{'$set': {'storage': ?1}}")
    void updateByStorageId(Long storageId, Storage storage);
}

package parser.app.webscraper.DAO;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.DAO.interfaces.StorageDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.StorageItemMapper;
import parser.app.webscraper.models.Storage;
import parser.userService.openapi.model.StorageDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StorageMongoTemplate implements StorageDao {
    private final MongoTemplate mongoTemplate;
    private final StorageItemMapper storageItemMapper;

    @Transactional
    @Override
    public Optional<Storage> findById(ObjectId id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Storage.class));
    }

    @Transactional
    @Override
    public Optional<Storage> findByUserId(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(id));
        return mongoTemplate
                .find(query, Storage.class)
                .stream()
                .filter(storage -> storage.getUserId().equals(id))
                .findFirst();
    }

    @Transactional
    @Override
    public Storage updateById(ObjectId id, StorageDTO storageDTO) {
        Storage storage = findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", id)));
        storage.setStorageItems(storageDTO.getStorageItems()
                .stream()
                .map(storageItemMapper::toStorageItem)
                .collect(Collectors.toList()));
        return mongoTemplate.save(storage);
    }

    @Transactional
    @Override
    public Storage save(Storage storage) {
        return mongoTemplate.save(storage);
    }
}

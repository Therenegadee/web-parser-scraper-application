package parser.app.webscraper.DAO.interfaces;

import org.bson.types.ObjectId;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.ParsingPreset;
import parser.app.webscraper.models.Storage;
import parser.userService.openapi.model.StorageDTO;

import java.util.Optional;

public interface StorageDao {

    Optional<Storage> findById(ObjectId id);

    Optional<Storage> findByUserId(Long id);

    Storage updateById(ObjectId id, StorageDTO storageDTO);

    Storage save(Storage storage);
}

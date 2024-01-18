package parser.app.webscraper.dao.jdbc.interfaces;

import parser.app.webscraper.models.Storage;

public interface StorageDao {
    Storage findByUserId(Long id);
}

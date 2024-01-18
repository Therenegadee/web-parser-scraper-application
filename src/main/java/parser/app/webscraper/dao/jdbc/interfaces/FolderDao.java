package parser.app.webscraper.dao.jdbc.interfaces;

import parser.app.webscraper.models.Folder;

import java.util.List;
import java.util.Optional;

public interface FolderDao {
    Optional<Folder> findByFolderId(Long id);

    List<Folder> findByFolderId(long maxId, long minId);

    Folder save(Folder folder);

    Folder update(Folder folder);

    Folder updateById(Long id, Folder folder);

    int deleteById(Long id);

    int delete(Folder folder);

}

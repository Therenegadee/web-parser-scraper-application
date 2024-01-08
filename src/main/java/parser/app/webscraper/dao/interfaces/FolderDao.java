package parser.app.webscraper.dao.interfaces;

import parser.app.webscraper.models.Folder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FolderDao {
    Optional<Folder> findByFolderId(Long id);

    List<Folder> findAllByUserId(Long id);

    List<Folder> findAllByParentFolderId(Long id);

    List<Folder> findAll();

    Folder save(Folder folder);

    Folder update(Folder folder);

    Folder updateById(Long id, Folder folder);

    int deleteById(Long id);

    int delete(Folder folder);

    int deleteAll();
}

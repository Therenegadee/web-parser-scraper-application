package parser.app.webscraper.dao.jdbc.interfaces;

import parser.app.webscraper.models.Folder;
import parser.userService.openapi.model.FolderOpenApi;

import java.util.List;
import java.util.Optional;

public interface FolderDao {
    Optional<Folder> findByFolderId(Long id);

    List<Folder> findByParentFolderId(Long id);

//    List<Folder> findByFolderId(long maxId, long minId);

    Folder save(FolderOpenApi folder);

    Folder update(FolderOpenApi folder);

    Folder updateById(Long id, FolderOpenApi folder);

    int deleteById(Long id);

    int delete(Folder folder);

}

package parser.app.webscraper.dao.jdbc;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.dao.jdbc.interfaces.FolderDao;
import parser.app.webscraper.dao.jdbc.interfaces.UserParserSettingsDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.jdbc.FolderRowMapper;
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.UserParserSetting;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

//TODO: пофиксить у всех ентити мапперы и инсерты/селекты из бд

@Observed
@Repository
@RequiredArgsConstructor
public class FolderJdbcTemplate implements FolderDao {
    private final JdbcTemplate jdbcTemplate;
    private final FolderRowMapper folderMapper;
    private final UserParserSettingsDao userParserSettingsDao;

    @Transactional
    @Override
    public Optional<Folder> findByFolderId(Long id) {
        String query = "SELECT * FROM folder f" +
                "WHERE id=?";
        return jdbcTemplate
                .query(query, folderMapper, id)
                .stream()
                .findFirst();
    }

    @Transactional
    @Override
    public List<Folder> findByFolderId(long minId, long maxId) {
        String query = "SELECT * FROM folder f" +
                "WHERE id BETWEEN ? AND ?";
        return jdbcTemplate.query(query, folderMapper, minId, maxId);
    }

    @Transactional
    @Override
    public Folder save(Folder folder) {
        if (Objects.isNull(folder)) throw new IllegalArgumentException("Folder is Null!");
        String query = "INSERT INTO folder (user_id,parent_folder_id) " +
                "VALUES(?,?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Long parentId = (folder.getParentFolder() != null) ? folder.getParentFolder().getId() : null;

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, folder.getName());
            Array tags = connection.createArrayOf("TEXT", folder.getTags().toArray());
            ps.setArray(index++, tags);
            ps.setLong(index++, folder.getStorage().getId());
            ps.setLong(index++, parentId);
            return ps;
        }, keyHolder);

        return findByFolderId(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new RuntimeException("Folder doesn't exist"));
    }

    @Transactional
    @Override
    public Folder update(Folder folder) {
        if (Objects.isNull(folder)) throw new IllegalArgumentException("Folder is Null!");
        Long id = folder.getId();
        return updateById(id, folder);
    }

    @Transactional
    @Override
    public Folder updateById(Long id, Folder folder) {
        if (Objects.isNull(folder)) throw new IllegalArgumentException("Folder is Null!");
        if (Objects.nonNull(id) && findByFolderId(id).isPresent()) {
            String query = "UPDATE folder SET name=?, tags=?, user_id=?, " +
                    "parent_folder=? WHERE id=?";
            Long parentId = (folder.getParentFolder() != null) ? folder.getParentFolder().getId() : null;
            int rows = jdbcTemplate.update(query, folder.getName(),
                    folder.getTags().stream().collect(Collectors.toList()),
                    folder.getUserId(), parentId, id);
            if (rows != 1) {
                throw new RuntimeException("Invalid request in SQL: " + query);
            }
            return findByFolderId(id).get();
        } else {
            throw new NotFoundException(String.format("Folder with id %d wasn't found", id));
        }
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        Optional<Folder> folderOpt = findByFolderId(id);
        if (Objects.nonNull(id) && folderOpt.isPresent()) {
            String query = "DELETE FROM folder WHERE id=?";
            Folder folder = folderOpt.get();
            List<UserParserSetting> userParserSettingsToUpdate = userParserSettingsDao.findAllByParentFolderId(id);
            userParserSettingsToUpdate
                    .forEach(userParserSetting -> userParserSetting.setParentFolder(
                            Objects.isNull(folder.getParentFolder()) ? null : folder.getParentFolder())
                    );
            userParserSettingsDao.update(userParserSettingsToUpdate);
            return jdbcTemplate.update(query, id);
        } else {
            throw new NotFoundException(String.format("Folder with id %d wasn't found", id));
        }
    }

    @Transactional
    @Override
    public int delete(Folder folder) {
        if (Objects.isNull(folder)) throw new IllegalArgumentException("Folder is Null!");
        Long id = folder.getId();
        return deleteById(id);
    }
}

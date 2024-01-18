package parser.app.webscraper.dao.jdbc;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.dao.jdbc.interfaces.StorageDao;
import parser.app.webscraper.mappers.jdbc.StorageMapper;
import parser.app.webscraper.models.Storage;

@Observed
@Repository
@RequiredArgsConstructor
public class StorageJdbcTemplate implements StorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final StorageMapper storageMapper;

    @Transactional
    @Override
    public Storage findByUserId(Long id) {
        String query = """
                WITH RECURSIVE storage_folder AS (
                     SELECT * FROM folder f
                     WHERE f.parent_folder_id IS NULL
                    \s
                     UNION ALL
                    \s
                     SELECT subf.* FROM folder subf
                     JOIN storage_folder sf ON subf.parent_folder_id = sf.id
                )
                SELECT sf.*, st.user_id FROM storage_folder sf
                JOIN storage st on st.id = sf.storage_id
                WHERE user_id = ?
                ORDER BY parent_folder_id NULLS FIRST, sf.id;
                """;
        return jdbcTemplate
                .query(query, storageMapper, id);
    }
}
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
                    JOIN folder f ON subf.parent_folder_id = f.id
                )
                SELECT * FROM storage_folder sf
                Join storage st on st.id = sf.storage_id
                WHERE user_id = 1
                ORDER BY parent_folder_id NULLS FIRST, sf.id;
                """;
        return jdbcTemplate
                .query(query, storageMapper, id);
    }
}



/*
SELECT f.id, f.name, f.tags, f.parent_folder_id, s.id, s.name, s.tags,
		s.first_page_url, s.num_of_pages_to_parse, s.class_name,
        s.tag_name, s.css_selector_next_page, s.header, s.parent_folder_id
      FROM folder f
LEFT JOIN user_parser_settings s
ON f.storage_id = s.storage_id
WHERE f.storage_id = 1
ORDER BY
CASE
   WHEN f.parent_folder_id IS NULL THEN 0
   ELSE 1
END,
f.parent_folder_id,
CASE
   WHEN s.parent_folder_id IS NULL THEN 0
   ELSE 1
END,
s.parent_folder_id
 */
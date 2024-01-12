package parser.app.webscraper.dao;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.dao.interfaces.StorageDao;
import parser.app.webscraper.mappers.jdbc.StorageRowMapper;
import parser.app.webscraper.models.Storage;

@Observed
@Repository
@RequiredArgsConstructor
public class StorageJdbcTemplate implements StorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final StorageRowMapper storageMapper;

    @Transactional
    @Override
    public Storage findByUserId(Long id) {
        String query = "SELECT f.id, f.name, f.tags, f.parent_folder_id, s.id, s.name, s.tags,\n" +
                "\t\ts.first_page_url, s.num_of_pages_to_parse, s.class_name,\n" +
                "        s.tag_name, s.css_selector_next_page, s.header, s.parent_folder_id\n" +
                "      FROM folder f\n" +
                "LEFT JOIN user_parser_settings s\n" +
                "ON f.storage_id = s.storage_id\n" +
                "WHERE f.storage_id = 1\n" +
                "ORDER BY \n" +
                "CASE \n" +
                "   WHEN f.parent_folder_id IS NULL THEN 0\n" +
                "   ELSE 1\n" +
                "END,\n" +
                "f.parent_folder_id,\n" +
                "CASE \n" +
                "   WHEN s.parent_folder_id IS NULL THEN 0\n" +
                "   ELSE 1\n" +
                "END,\n" +
                "s.parent_folder_id\n";
        return jdbcTemplate.query(query, storageMapper, id);
    }
}

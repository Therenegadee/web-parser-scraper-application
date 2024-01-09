package parser.app.webscraper.dao;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.dao.interfaces.ElementLocatorDao;
import parser.app.webscraper.dao.interfaces.ParserResultDao;
import parser.app.webscraper.dao.interfaces.UserParserSettingsDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.jdbc.UserParserSettingRowMapper;
import parser.app.webscraper.models.UserParserSetting;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Observed
@Repository
@RequiredArgsConstructor
public class UserParserSettingsJdbcTemplate implements UserParserSettingsDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserParserSettingRowMapper settingMapper;
    private final ElementLocatorDao elementLocatorDao;
    private final ParserResultDao parserResultDao;

    @Transactional
    @Override
    public Optional<UserParserSetting> findById(Long id) {
        String query = "SELECT * FROM user_parser_settings WHERE id=?";
        return jdbcTemplate.query(query, settingMapper, id).stream().findFirst();
    }

    @Override
    public List<UserParserSetting> findAllByParentFolderId(Long id) {
        String query = "SELECT * FROM user_parser_settings WHERE parent_folder_id=?";
        return new ArrayList<>(jdbcTemplate
                .query(query, settingMapper, id));
    }

    @Transactional
    @Override
    public UserParserSetting save(UserParserSetting userParserSetting) {
        if (Objects.isNull(userParserSetting)) throw new IllegalArgumentException("UserParserSetting is Null!");
        String query = "INSERT INTO user_parser_settings (first_page_url, num_of_pages_to_parse, class_name, tag_name, " +
                "css_selector_next_page, header, parent_folder_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        Long parentFolderId = (userParserSetting.getParentFolder() != null) ? userParserSetting.getParentFolder().getId() : null;

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, userParserSetting.getFirstPageUrl());
            ps.setInt(index++, userParserSetting.getNumOfPagesToParse());
            ps.setString(index++, userParserSetting.getClassName());
            ps.setString(index++, userParserSetting.getTagName());
            ps.setString(index++, userParserSetting.getCssSelectorNextPage());
            ps.setArray(index++, connection.createArrayOf("TEXT", userParserSetting.getHeader().toArray()));
            ps.setLong(index++, parentFolderId);
            return ps;
        }, keyHolder);

        elementLocatorDao.saveAll(userParserSetting.getElementLocators(), keyHolder.getKey().longValue());

        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new RuntimeException("UserParserSetting doesn't exist"));
    }

    @Transactional
    @Override
    public UserParserSetting update(UserParserSetting userParserSetting) {
        if (Objects.isNull(userParserSetting)) throw new IllegalArgumentException("UserParserSetting is Null!");
        Long id = userParserSetting.getId();
        return updateById(id, userParserSetting);
    }

    @Transactional
    @Override
    public UserParserSetting updateById(Long id, UserParserSetting userParserSetting) {
        if (Objects.isNull(userParserSetting)) throw new IllegalArgumentException("UserParserSetting is Null!");
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "UPDATE user_parser_settings SET first_page_url=?, num_of_pages_to_parse=?, class_name=?, " +
                    "tag_name=?, css_selector_next_page=?, header=?, parent_folder_id=? WHERE id=?";
            Long parentFolderId = (userParserSetting.getParentFolder() != null) ? userParserSetting.getParentFolder().getId() : null;
            int rows = jdbcTemplate.update(query, userParserSetting.getFirstPageUrl(),
                    userParserSetting.getNumOfPagesToParse(), userParserSetting.getClassName(),
                    userParserSetting.getTagName(), userParserSetting.getCssSelectorNextPage(),
                    userParserSetting.getHeader(), parentFolderId, id);

            parserResultDao.updateAll(userParserSetting.getParsingHistory());
            elementLocatorDao.updateAll(userParserSetting.getElementLocators());

            if (rows != 1) {
                throw new RuntimeException("Invalid request in SQL: " + query);
            }
            return findById(id).get();
        } else {
            throw new NotFoundException(String.format("UserParserSetting with id %d wasn't found", id));
        }
    }

    @Override
    public void update(List<UserParserSetting> userParserSetting) {
        userParserSetting.forEach(this::update);
    }

    @Transactional
    @Override
    public List<UserParserSetting> findAll() {
        String query = "SELECT * FROM user_parser_settings";
        return new ArrayList<>(jdbcTemplate.query(query, settingMapper));
    }

    @Transactional
    @Override
    public List<UserParserSetting> findAllByUserId(Long id) {
        String query = "SELECT * FROM user_parser_settings WHERE user_id=?";
        return new ArrayList<>(jdbcTemplate.query(query, settingMapper, id));
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "DELETE FROM user_parser_settings WHERE id=?";
            return jdbcTemplate.update(query, id);
        } else {
            throw new NotFoundException(String.format("UserParserSetting with id %d wasn't found", id));
        }
    }

    @Transactional
    @Override
    public int delete(UserParserSetting userParserSetting) {
        if (Objects.isNull(userParserSetting)) throw new IllegalArgumentException("UserParserSetting is Null!");
        Long id = userParserSetting.getId();
        return deleteById(id);
    }

    @Transactional
    @Override
    public int deleteAll() {
        String query = "DELETE FROM user_parser_settings";
        return jdbcTemplate.update(query);
    }
}

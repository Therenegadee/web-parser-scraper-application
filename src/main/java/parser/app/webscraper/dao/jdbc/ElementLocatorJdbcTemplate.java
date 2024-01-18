package parser.app.webscraper.dao.jdbc;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.dao.jdbc.interfaces.ElementLocatorDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.jdbc.ElementLocatorRowMapper;
import parser.app.webscraper.models.ElementLocator;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Observed
@Repository
@RequiredArgsConstructor
public class ElementLocatorJdbcTemplate implements ElementLocatorDao {
    private final JdbcTemplate jdbcTemplate;
    private final ElementLocatorRowMapper elementMapper;

    @Transactional
    @Override
    public Optional<ElementLocator> findById(Long id) {
        String query = "SELECT * FROM element_locator WHERE id=?";
        return jdbcTemplate
                .query(query, elementMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<ElementLocator> findByName(String name) {
        String query = "SELECT * FROM element_locator WHERE name=?";
        return jdbcTemplate
                .query(query, elementMapper, name)
                .stream()
                .findFirst();
    }

    @Transactional
    @Override
    public ElementLocator save(ElementLocator elementLocator, Long settingsId) {
        if (Objects.isNull(elementLocator)) throw new IllegalArgumentException("ElementLocator is Null!");
        String query = "INSERT INTO element_locator (name,type,path_to_locator,extra_pointer,user_parser_settings_id)" +
                " VALUES(?,?,?,?,?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, elementLocator.getName());
            ps.setString(index++, elementLocator.getType().getValue());
            ps.setString(index++, elementLocator.getPathToLocator());
            ps.setString(index++, elementLocator.getExtraPointer());
            ps.setLong(index++, settingsId);
            return ps;
        }, keyHolder);

        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new RuntimeException("Element Locator doesn't exist"));
    }

    @Transactional
    @Override
    public List<ElementLocator> saveAll(List<ElementLocator> elementLocators, Long settingsId) {
        elementLocators.forEach(elementLocator -> save(elementLocator, settingsId));
        return findAllByParserSettingsId(settingsId).stream().toList();
    }

    @Transactional
    @Override
    public void updateAll(List<ElementLocator> elementLocators) {
        elementLocators.forEach(this::update);
    }

    @Transactional
    @Override
    public ElementLocator update(ElementLocator elementLocator) {
        if (Objects.isNull(elementLocator)) throw new IllegalArgumentException("ElementLocator is Null!");
        Long id = elementLocator.getId();
        return updateById(id, elementLocator);
    }

    @Transactional
    @Override
    public ElementLocator updateById(Long id, ElementLocator elementLocator) {
        if (Objects.isNull(elementLocator)) throw new IllegalArgumentException("ElementLocator is Null!");
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "UPDATE element_locator SET name=?,type=?,path_to_locator=?,extra_pointer=?," +
                    "user_parser_settings_id=? WHERE id=?";
            int rows = jdbcTemplate.update(query, elementLocator.getName(),
                    elementLocator.getType().getValue(), elementLocator.getPathToLocator(),
                    elementLocator.getExtraPointer(), elementLocator.getUserParserSetting().getId());
            if (rows != 1) {
                throw new RuntimeException("Invalid request in sql: " + query);
            }
            return findById(id).get();
        } else {
            throw new NotFoundException(String.format("ElementLocator with id %d wasn't found", id));
        }
    }

    @Transactional
    @Override
    public List<ElementLocator> findAll() {
        String query = "SELECT * FROM element_locator";
        return new ArrayList<>(jdbcTemplate.query(query, elementMapper));
    }

    @Transactional
    @Override
    public List<ElementLocator> findAllByParserSettingsId(Long id) {
        String query = "SELECT * FROM element_locator WHERE user_parser_settings_id=?";
        return new ArrayList<>(jdbcTemplate.query(query, elementMapper, id));
    }


    @Transactional
    @Override
    public int delete(ElementLocator elementLocator) {
        if (Objects.isNull(elementLocator)) throw new IllegalArgumentException("ElementLocator is Null!");
        Long id = elementLocator.getId();
        return deleteById(id);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "DELETE FROM element_locator WHERE id=?";
            return jdbcTemplate.update(query, id);
        } else {
            throw new NotFoundException(String.format("ElementLocator with id %d wasn't found", id));
        }
    }

    @Transactional
    @Override
    public int deleteAll() {
        String query = "DELETE FROM element_locator";
        return jdbcTemplate.update(query);
    }
}

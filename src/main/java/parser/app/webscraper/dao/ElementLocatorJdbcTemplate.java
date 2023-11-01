package parser.app.webscraper.dao;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.dao.interfaces.ElementLocatorDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.jdbc.ElementLocatorRowMapper;
import parser.app.webscraper.models.ElementLocator;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

    @Transactional
    @Override
    public ElementLocator save(ElementLocator elementLocator) {
        if (Objects.isNull(elementLocator)) throw new IllegalArgumentException("ElementLocator is Null!");
        String query = "INSERT INTO element_locator (type,path_to_locator,extra_pointer,user_parser_settings_id)" +
                " VALUES(?,?,?,?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, elementLocator.getType().getValue());
            ps.setString(index++, elementLocator.getPathToLocator());
            ps.setString(index++, elementLocator.getExtraPointer());
            ps.setLong(index++, elementLocator.getUserParserSetting().getId());
            return ps;
        }, keyHolder);

        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new RuntimeException("Element Locator doesn't exist"));
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
            String query = "UPDATE element_locator SET type=?,path_to_locator=?,extra_pointer=?," +
                    "user_parser_settings_id=? WHERE id=?";
            int rows = jdbcTemplate.update(query, elementLocator.getType().getValue(), elementLocator.getPathToLocator(),
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
    public Set<ElementLocator> findAll() {
        String query = "SELECT * FROM element_locator";
        return new HashSet<>(jdbcTemplate.query(query, elementMapper));
    }

    @Transactional
    @Override
    public Set<ElementLocator> findAllByParserSettingsId(Long id) {
        String query = "SELECT * FROM element_locator WHERE user_parser_settings_id=?";
        return new HashSet<>(jdbcTemplate.query(query, elementMapper, id));
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

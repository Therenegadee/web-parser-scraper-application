package parser.app.webscraper.dao;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.dao.interfaces.ParserResultDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.jdbc.ParserResultRowMapper;
import parser.app.webscraper.models.ParserResult;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Observed
@Repository
@RequiredArgsConstructor
public class ParserResultJdbcTemplate implements ParserResultDao {
    private final JdbcTemplate jdbcTemplate;
    private final ParserResultRowMapper parserResultMapper;

    @Transactional
    @Override
    public Optional<ParserResult> findById(Long id) {
        String query = "SELECT * FROM parser_results WHERE id=?";
        return jdbcTemplate
                .query(query, parserResultMapper, id)
                .stream()
                .findAny();
    }

    @Transactional
    @Override
    public List<ParserResult> findAllByParserSettingsId(Long id) {
        String query = "SELECT * FROM parser_results WHERE user_parser_settings_id=?";
        return new ArrayList<>(jdbcTemplate.query(query, parserResultMapper, id));
    }

    @Transactional
    @Override
    public ParserResult save(ParserResult parserResult) {
        if (Objects.isNull(parserResult)) throw new IllegalArgumentException("Parser Result is Null!");
        String query = "INSERT INTO parser_results " +
                "(date,link_to_download,output_file_type,user_parser_settings_id) " +
                "VALUES(?,?,?,?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setDate(index++, new Date(parserResult.getDate().getTime()));
            ps.setString(index++, parserResult.getLinkToDownloadResults());
            ps.setString(index++, parserResult.getOutputFileType().getValue());
            ps.setLong(index++, parserResult.getUserParserSetting().getId());
            return ps;
        }, keyHolder);

        return findById(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new RuntimeException("Parser Result doesn't exist"));
    }

    @Transactional
    @Override
    public ParserResult update(ParserResult parserResult) {
        if (Objects.isNull(parserResult)) throw new IllegalArgumentException("Parse Result is Null!");
        Long id = parserResult.getId();
        return updateById(id, parserResult);
    }

    @Transactional
    @Override
    public ParserResult updateById(Long id, ParserResult parserResult) {
        if (Objects.isNull(parserResult)) throw new IllegalArgumentException("Parser Result is Null!");
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "UPDATE parser_results SET date=?,link_to_download=?," +
                    "output_file_type=?,user_parser_settings_id=? WHERE id=?";
            int rows = jdbcTemplate.update(query, parserResult.getDate(),
                    parserResult.getLinkToDownloadResults(), parserResult.getOutputFileType(),
                    parserResult.getUserParserSetting().getId(), id);
            if (rows != 1) {
                throw new RuntimeException("Invalid request in SQL: " + query);
            }
            return findById(id).get();
        } else {
            throw new NotFoundException(String.format("Parser Result with id %d wasn't found", id));
        }
    }

    @Override
    public void updateAll(List<ParserResult> parsingHistory) {
        parsingHistory.forEach(this::update);
    }

    @Transactional
    @Override
    public List<ParserResult> findAll() {
        String query = "SELECT * FROM parser_results";
        return new ArrayList<>(jdbcTemplate.query(query, parserResultMapper));
    }

    @Transactional
    @Override
    public List<ParserResult> findAllByUserId(Long id) {
        if (Objects.nonNull(id)) {
            String query = "SELECT * FROM parser_results WHERE user_id=?";
            return new ArrayList<>(jdbcTemplate.query(query, parserResultMapper, id));
        } else {
            throw new IllegalStateException("The id is null!");
        }
    }

    @Transactional
    @Override
    public List<ParserResult> findAllByIds(List<Long> ids) {
        if (Objects.nonNull(ids) && !ids.isEmpty()) {
            String query = "SELECT * FROM parser_results WHERE id IN (?)";
            return new ArrayList<>(jdbcTemplate.query(query, parserResultMapper, ids));
        } else {
            throw new IllegalStateException("The ids are null!");
        }
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        if (Objects.nonNull(id) && findById(id).isPresent()) {
            String query = "DELETE FROM parser_results WHERE id=?";
            return jdbcTemplate.update(query, id);
        } else {
            throw new NotFoundException(String.format("ParserResult with id %d wasn't found", id));
        }
    }

    @Transactional
    @Override
    public int delete(ParserResult parserResult) {
        if (Objects.isNull(parserResult)) throw new IllegalArgumentException("ParserResult is Null!");
        Long id = parserResult.getId();
        return deleteById(id);
    }

    @Transactional
    @Override
    public int deleteAll() {
        String query = "DELETE FROM parser_results";
        return jdbcTemplate.update(query);
    }
}

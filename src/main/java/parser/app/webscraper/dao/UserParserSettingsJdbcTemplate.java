package parser.app.webscraper.dao;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.dao.interfaces.UserParserSettingsDao;
import parser.app.webscraper.models.UserParserSetting;

import java.util.Optional;
import java.util.Set;

@Observed
@Repository
@RequiredArgsConstructor
public class UserParserSettingsJdbcTemplate implements UserParserSettingsDao {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public Optional<UserParserSetting> findById(Long id) {
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<UserParserSetting> findByParserResultId(Long id) {
        return Optional.empty();
    }

    @Transactional
    @Override
    public UserParserSetting save(UserParserSetting userParserSetting) {
        return null;
    }

    @Transactional
    @Override
    public UserParserSetting update(UserParserSetting userParserSetting) {
        return null;
    }

    @Transactional
    @Override
    public UserParserSetting updateById(Long id, UserParserSetting userParserSetting) {
        return null;
    }

    @Transactional
    @Override
    public Set<UserParserSetting> findAll() {
        return null;
    }

    @Transactional
    @Override
    public Set<UserParserSetting> findAllByUserId(Long id) {
        return null;
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Transactional
    @Override
    public int delete(UserParserSetting userParserSetting) {
        return 0;
    }

    @Transactional
    @Override
    public int deleteAll() {
        return 0;
    }
}

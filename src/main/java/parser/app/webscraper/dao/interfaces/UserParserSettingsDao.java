package parser.app.webscraper.dao.interfaces;

import org.springframework.stereotype.Repository;
import parser.app.webscraper.models.UserParserSetting;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserParserSettingsDao {
    Optional<UserParserSetting> findById(Long id);

    List<UserParserSetting> findAllByParentFolderId(Long id);

    List<UserParserSetting> findAll();

    List<UserParserSetting> findAllByUserId(Long id);

    UserParserSetting save(UserParserSetting userParserSetting);

    UserParserSetting update(UserParserSetting userParserSetting);

    void update(List<UserParserSetting> userParserSetting);

    UserParserSetting updateById(Long id, UserParserSetting userParserSetting);

    int deleteById(Long id);

    int delete(UserParserSetting userParserSetting);

    int deleteAll();
}

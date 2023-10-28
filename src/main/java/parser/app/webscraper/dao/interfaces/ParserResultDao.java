package parser.app.webscraper.dao.interfaces;

import org.springframework.stereotype.Repository;
import parser.app.webscraper.models.ParserResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ParserResultDao {
    Optional<ParserResult> findById(Long id);
    Optional<ParserResult> findByParserSettingsId(Long id);
    ParserResult save(ParserResult parserResult);

    ParserResult update(ParserResult parserResult);

    ParserResult updateById(Long id, ParserResult parserResult);

    Set<ParserResult> findAll();

    Set<ParserResult> findAllByUserId(Long id);
    Set<ParserResult> findAllByIds(List<Long> ids);

    int deleteById(Long id);

    int delete(ParserResult parserResult);

    int deleteAll();
}

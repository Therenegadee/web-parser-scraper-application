package parser.app.webscraper.dao.jdbc.interfaces;

import org.springframework.stereotype.Repository;
import parser.app.webscraper.models.ParserResult;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParserResultDao {
    Optional<ParserResult> findById(Long id);
    List<ParserResult> findAllByParserSettingsId(Long id);
    List<ParserResult> findAll();
    List<ParserResult> findAllByUserId(Long id);
    List<ParserResult> findAllByIds(List<Long> ids);
    ParserResult save(ParserResult parserResult);

    ParserResult update(ParserResult parserResult);

    ParserResult updateById(Long id, ParserResult parserResult);

    void updateAll(List<ParserResult> parsingHistory);

    int deleteById(Long id);

    int delete(ParserResult parserResult);

    int deleteAll();
}

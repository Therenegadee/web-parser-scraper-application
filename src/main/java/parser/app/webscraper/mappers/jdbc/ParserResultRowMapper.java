package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import parser.app.webscraper.dao.interfaces.UserParserSettingsDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.ParserResult;
import parser.app.webscraper.models.UserParserSetting;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class ParserResultRowMapper implements RowMapper<ParserResult> {

    private final UserParserSettingsDao userParserSettingsDao;

    @Override
    public ParserResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ParserResult
                .builder()
                .id(rs.getLong("id"))
                .userParserSettings(getParserSettingsById(rs.getLong("user_parser_settings_id")))
                .linkToDownloadResults(rs.getString("link_to_download"))
                .build();
    }

    private UserParserSetting getParserSettingsById(Long id) {
        return userParserSettingsDao.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Parser Settings with id %d wasn't found", id)));
    }

}

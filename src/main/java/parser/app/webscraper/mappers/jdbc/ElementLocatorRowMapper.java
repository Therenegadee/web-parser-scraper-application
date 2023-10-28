package parser.app.webscraper.mappers.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import parser.app.webscraper.dao.interfaces.UserParserSettingsDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.ElementLocator;
import parser.app.webscraper.models.UserParserSetting;
import parser.app.webscraper.models.enums.ElementType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class ElementLocatorRowMapper implements RowMapper<ElementLocator> {

    private final UserParserSettingsDao userParserSettingsDao;

    @Override
    public ElementLocator mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ElementLocator.builder()
                .id(rs.getLong("id"))
                .type(ElementType.fromValue(rs.getString("type")))
                .pathToLocator(rs.getString("path_to_locator"))
                .extraPointer(rs.getString("exta_pointer"))
                .userParserSetting(getParserSettingsById(rs.getLong("user_parser_settings_id")))
                .build();
    }

    private UserParserSetting getParserSettingsById(Long id){
        return userParserSettingsDao.findById(id)
                .orElseThrow(
                        ()-> new NotFoundException(String.format("Parser Settings with id %d wasn't found", id)));
    }

}

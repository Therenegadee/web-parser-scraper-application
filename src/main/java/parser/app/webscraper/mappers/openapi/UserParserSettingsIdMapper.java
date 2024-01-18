package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.dao.jdbc.interfaces.UserParserSettingsDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.UserParserSetting;

import java.util.Objects;

@Mapper(componentModel = "spring")
public class UserParserSettingsIdMapper {
    @Autowired
    private UserParserSettingsDao userParserSettingsDao;

    public UserParserSetting getUserParserSettingById(Long id) {
        if (id == null) {
            return null;
        }
        return userParserSettingsDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("UserParserSetting with id %d wasn't found", id)));
    }

    public Long getId(UserParserSetting userParserSetting){
        if (Objects.isNull(userParserSetting)) {
            return null;
        }
        return userParserSetting.getId();
    }
}

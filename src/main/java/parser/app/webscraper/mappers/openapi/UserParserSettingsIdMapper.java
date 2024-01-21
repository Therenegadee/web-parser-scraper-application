package parser.app.webscraper.mappers.openapi;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.UserParserSetting;
import parser.app.webscraper.repository.StorageRepository;

import java.util.Objects;
import java.util.UUID;

@Mapper(componentModel = "spring")
public class UserParserSettingsIdMapper {
    @Autowired
    private StorageRepository storageRepository;

    public UserParserSetting getUserParserSettingById(UUID id, UUID storageId) {
        if (id == null) {
            return null;
        }
        return (UserParserSetting) storageRepository.findStorageItemById(storageId, id)
                .orElseThrow(() -> new NotFoundException(String.format("UserParserSetting with id %d wasn't found", id)));
    }

    public UUID getId(UserParserSetting userParserSetting){
        if (Objects.isNull(userParserSetting)) {
            return null;
        }
        return userParserSetting.getId();
    }
}

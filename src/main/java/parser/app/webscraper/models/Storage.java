package parser.app.webscraper.models;

import io.micrometer.observation.annotation.Observed;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Document("storages")
@Setter
@Getter
@Component
public class Storage {
    @Id
    private String id;
    private Long userId;
    private List<StorageItem> storageItems;

    public Storage() {
        this.storageItems = new ArrayList<>();
    }

    public final void addStorageItem(StorageItem storageItem) {
        if(Objects.isNull(storageItem.getId())) {
            storageItem.setId(IdGenerator.generateId(storageItem));
        }
        storageItems.add(storageItem);
    }

    public final void addStorageItems(List<StorageItem> storageItems) {
        storageItems.forEach(this::addStorageItem);
    }

    public final void addStorageItemInsideFolder(StorageItem storageItem, String folderId) {
        Folder folder = findFolderById(folderId)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with name %s wasn't found", folderId)));
        folder.addStorageItem(storageItem);
    }

    @Observed
    public final Optional<UserParserSetting> findParserSettingsById(String settingsId) {
        for (StorageItem item : this.storageItems) {
            if (item instanceof UserParserSetting && ((UserParserSetting) item).getId().equals(settingsId)) {
                return Optional.of((UserParserSetting) item);
            } else if (item instanceof Folder) {
                Optional<UserParserSetting> result = ((Folder) item).findParserSettingsById(settingsId);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }

    @Observed
    public final Optional<Folder> findFolderById(String folderId) {
        for (StorageItem item : this.storageItems) {
            if (item instanceof Folder) {
                if (((Folder) item).getId().equals(folderId)) {
                    return Optional.of((Folder) item);
                }
                Optional<Folder> result = ((Folder) item).findFolderById(folderId);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }
}

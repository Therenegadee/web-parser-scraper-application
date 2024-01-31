package parser.app.webscraper.models;

import com.mongodb.lang.Nullable;
import io.micrometer.observation.annotation.Observed;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import parser.app.webscraper.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Component
public class Folder extends StorageItem {
    private String storageId;
    @Nullable
    private String parentFolderId;
    private List<StorageItem> folderItems;

    public Folder() {
        this.folderItems = new ArrayList<>();
    }

    @Builder
    public Folder(
            String id,
            String name,
            List<String> tags,
            String storageId,
            String parentFolderId,
            List<StorageItem> folderItems
    ) {
        super(id,name, tags);
        this.storageId = storageId;
        this.parentFolderId = parentFolderId;
        if (Objects.isNull(folderItems)) {
            this.folderItems = new ArrayList<>();
        }
        this.folderItems = folderItems;
    }

    public final void addStorageItem(StorageItem storageItem) {
        if(Objects.isNull(storageItem.getId())) {
            storageItem.setId(IdGenerator.generateId(storageItem));
        }
        this.folderItems.add(storageItem);
    }

    @Observed
    public final Optional<UserParserSetting> findParserSettingsById(String settingsId) {
        for (StorageItem item : this.folderItems) {
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
        for (StorageItem item : this.folderItems) {
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

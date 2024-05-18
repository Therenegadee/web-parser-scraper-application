package parser.app.webscraper.models;

import io.micrometer.observation.annotation.Observed;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import parser.app.webscraper.exceptions.NotFoundException;

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
    private ObjectId id;
    private Long userId;
    private List<StorageItem> storageItems;

    public Storage() {
        this.storageItems = new ArrayList<>();
    }

    public final void addStorageItem(StorageItem storageItem) {
        if (Objects.isNull(storageItem.getId())) {
            storageItem.setId(ObjectId.get());
        }
        storageItems.add(storageItem);
    }

    public final void addStorageItems(List<StorageItem> storageItems) {
        storageItems.forEach(this::addStorageItem);
    }

    public final void addStorageItemInsideFolder(StorageItem storageItem, ObjectId folderId) {
        Folder folder = findFolderById(folderId)
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s wasn't found", folderId)));
        folder.addFolderItem(storageItem);
    }

    @Observed
    public final Optional<ParsingPreset> findParserSettingsById(ObjectId settingsId) {
        for (StorageItem item : this.storageItems) {
            if (item instanceof ParsingPreset && item.getId().equals(settingsId)) {
                return Optional.of((ParsingPreset) item);
            } else if (item instanceof Folder) {
                Optional<ParsingPreset> result = ((Folder) item).findParserSettingsById(settingsId);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }

    @Observed
    public final Optional<Folder> findFolderById(ObjectId folderId) {
        for (StorageItem item : this.storageItems) {
            if (item instanceof Folder) {
                if (item.getId().equals(folderId)) {
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

    @Observed
    public final Optional<StorageItem[]> findSettingsWithParentFolder(ObjectId settingsId) {
        StorageItem[] settingsWithParentFolder = new StorageItem[2];
        for (StorageItem item : this.storageItems) {
            if (item instanceof Folder) {
                Optional<ParsingPreset> result = ((Folder) item).findParserSettingsById(settingsId);
                if (result.isPresent()) {
                    settingsWithParentFolder[0] = item;
                    settingsWithParentFolder[1] = result.get();
                    return Optional.of(settingsWithParentFolder);
                }
            }
        }
        return Optional.empty();
    }

    @Observed
    public final Optional<Folder[]> findFolderWithParentFolder(ObjectId folderId) {
        for (StorageItem item : this.storageItems) {
            if (item instanceof Folder) {
                if (item.getId().equals(folderId)) {
                    Folder[] folderWithParentFolder = new Folder[2];
                    folderWithParentFolder[0] = null;
                    folderWithParentFolder[1] = (Folder) item;
                    return Optional.of(folderWithParentFolder);
                } else {
                    return ((Folder) item).findFolderWithParentFolder(folderId);
                }
            }
        }
        return Optional.empty();
    }
}

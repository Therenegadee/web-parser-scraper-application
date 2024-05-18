package parser.app.webscraper.models;

import com.mongodb.lang.Nullable;
import io.micrometer.observation.annotation.Observed;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Component
public class Folder extends StorageItem {
    private ObjectId storageId;
    @Nullable
    private ObjectId parentFolderId;
    private List<StorageItem> folderItems;

    public Folder() {
        this.folderItems = new ArrayList<>();
    }

    @Builder
    public Folder(
            ObjectId id,
            String name,
            List<String> tags,
            ObjectId storageId,
            @Nullable ObjectId parentFolderId,
            List<StorageItem> folderItems
    ) {
        super(id, name, tags);
        this.storageId = storageId;
        this.parentFolderId = parentFolderId;
        this.folderItems = folderItems;
    }

    public final void addFolderItem(StorageItem storageItem) {
        if (Objects.isNull(storageItem.getId())) {
            storageItem.setId(ObjectId.get());
        }
        this.folderItems.add(storageItem);
    }

    @Observed
    public final Optional<ParsingPreset> findParserSettingsById(ObjectId settingsId) {
        for (StorageItem item : this.folderItems) {
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
        for (StorageItem item : this.folderItems) {
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
    public final Optional<Folder[]> findFolderWithParentFolder(ObjectId folderId) {
        for(StorageItem item : this.folderItems) {
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

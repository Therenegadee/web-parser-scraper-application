package parser.app.webscraper.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import parser.app.webscraper.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Component
public class Folder extends StorageItem {
    private String storageId;
    private String parentFolderId;
    private List<StorageItem> storageItems;

    public Folder() {
        this.storageItems = new ArrayList<>();
    }

    @Builder
    public Folder(
            String id,
            String name,
            List<String> tags,
            String storageId,
            String parentFolderId,
            List<StorageItem> storageItems
    ) {
        super(id, name, tags);
        this.storageId = storageId;
        this.parentFolderId = parentFolderId;
        if (Objects.isNull(storageItems)) {
            this.storageItems = new ArrayList<>();
        }
        this.storageItems = storageItems;
    }

    public void addStorageItem(StorageItem storageItem) {
        if(Objects.isNull(storageItem.getId())) {
            storageItem.setId(IdGenerator.generateId(storageItem));
        }
        storageItems.add(storageItem);
    }
}

package parser.app.webscraper.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Component
public class Folder extends StorageItem {
    @Id
    private UUID id;
    private UUID storageId;
    private UUID parentFolderId;
    private List<StorageItem> storageItems;

    public Folder() {
        this.storageItems = new ArrayList<>();
    }

    public void addStorageItem(StorageItem storageItem) {
        storageItems.add(storageItem);
    }
}

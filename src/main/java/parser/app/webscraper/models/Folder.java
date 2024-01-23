package parser.app.webscraper.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Folder extends StorageItem {
    @Id
    private UUID id;
    private UUID storageId;
    private UUID parentFolderId;
    private List<StorageItem> storageItems;

    public void addStorageItem(StorageItem storageItem) {
        storageItems.add(storageItem);
    }
}

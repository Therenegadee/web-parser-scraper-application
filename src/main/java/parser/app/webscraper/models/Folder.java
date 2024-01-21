package parser.app.webscraper.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Folder extends StorageItem {
    @Id
    private Long id;
    private Storage storage;
    private Folder parentFolder;
    private List<StorageItem> storageItems;

    public void addStorageItem(StorageItem storageItem) {
        storageItems.add(storageItem);
    }
}

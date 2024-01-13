package parser.app.webscraper.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder extends StorageItem {
    private Long id;
    private Storage storage;
    private Folder parentFolder;
    private List<StorageItem> storageItems;

    public void addStorageItem(StorageItem storageItem) {
        storageItems.add(storageItem);
    }
}

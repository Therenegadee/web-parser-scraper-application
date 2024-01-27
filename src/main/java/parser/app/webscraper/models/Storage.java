package parser.app.webscraper.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import parser.app.webscraper.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document("storages")
@Setter
@Getter
@Component
public class Storage {
    @Id
    private UUID id;
    private Long userId;
    private List<StorageItem> storageItems;

    public Storage() {
        this.storageItems = new ArrayList<>();
    }

    public final void addStorageItem(StorageItem storageItem) {
        storageItems.add(storageItem);
    }

    public final void addStorageItems(List<StorageItem> storageItem) {
        storageItems.addAll(storageItem);
    }

    public final void addStorageItemInsideFolder(StorageItem storageItem, String folderName) {
        Folder folder = (Folder) storageItems
                .stream()
                .filter(item -> item.getName().equals(folderName))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Folder with name %s wasn't found", folderName)));
        folder.addStorageItem(storageItem);
    }

    public boolean isNew(){
        return (getId() == null);
    }
}

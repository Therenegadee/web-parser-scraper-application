package parser.app.webscraper.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Document
@Setter
@Getter
@Component
public class Storage {
    @Id
    private UUID id;
    private Long userId;
    private List<StorageItem> storageItems;
}

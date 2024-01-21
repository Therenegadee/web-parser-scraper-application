package parser.app.webscraper.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Document
@Setter
@Getter
@Component
public class Storage {
    @Id
    private Long id;
    private Long userId;
    private List<StorageItem> storageItems;
}

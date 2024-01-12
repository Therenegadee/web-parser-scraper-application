package parser.app.webscraper.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
public class Storage {
    private Long id;
    private Long userId;
    private List<StorageItem> storageItems;
}

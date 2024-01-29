package parser.app.webscraper.models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class StorageItem {
    @Id
    private String id;
    private String name;
    private List<String> tags;
}

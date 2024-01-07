package parser.app.webscraper.models;

import lombok.*;

import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FolderItem {
    private String name;
    private HashSet<String> tags;
}

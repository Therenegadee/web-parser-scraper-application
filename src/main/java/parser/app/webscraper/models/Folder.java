package parser.app.webscraper.models;

import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder extends FolderItem {
    private Long id;
    private Long userId;
    private Optional<Folder> parentFolder;
    private List<FolderItem> folderItems;
}

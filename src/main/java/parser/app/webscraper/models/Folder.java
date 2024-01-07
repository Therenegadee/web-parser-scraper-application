package parser.app.webscraper.models;

import lombok.*;

import java.util.HashSet;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder {
    private Long id;
    private Long userId;
    private Folder parentFolder;
    private HashSet<UserParserSetting> folderItems;
}

package parser.app.webscraper.services.interfaces;

import parser.app.webscraper.models.FolderItem;
import parser.userService.openapi.model.FolderItemOpenApi;

import java.util.List;

public interface FolderService {
    List<FolderItemOpenApi> getAllFolderItemsByUserId(Long userId);
}

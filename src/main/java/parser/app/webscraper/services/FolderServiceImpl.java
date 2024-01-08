package parser.app.webscraper.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parser.app.webscraper.dao.interfaces.FolderDao;
import parser.app.webscraper.dao.interfaces.UserParserSettingsDao;
import parser.app.webscraper.mappers.openapi.FolderItemMapper;
import parser.app.webscraper.models.FolderItem;
import parser.app.webscraper.services.interfaces.FolderService;
import parser.userService.openapi.model.FolderItemOpenApi;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderDao folderDao;
    private final UserParserSettingsDao userParserSettingsDao;
    private final FolderItemMapper folderItemMapper;

    @Override
    public List<FolderItemOpenApi> getAllFolderItemsByUserId(Long userId) {
        List<FolderItem> folderItems = new ArrayList<>();
        folderItems.addAll(folderDao.findAllByUserId(userId));
        folderItems.addAll(userParserSettingsDao.findAllByUserId(userId)
                .stream()
                .filter(settings -> settings.getParentFolder() == null)
                .toList()
        );
        return folderItemMapper.toOpenApi(folderItems);
    }
}

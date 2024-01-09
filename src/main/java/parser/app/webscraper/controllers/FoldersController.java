package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parser.app.webscraper.models.FolderItem;
import parser.app.webscraper.services.interfaces.FolderService;
import parser.userService.openapi.api.FolderApiDelegate;
import parser.userService.openapi.model.FolderItemOpenApi;
import parser.userService.openapi.model.FolderOpenApi;

import java.util.List;

@RestController
@RequestMapping("/api/folder")
@RequiredArgsConstructor
public class FoldersController implements FolderApiDelegate {
    private final FolderService folderService;

//    @Observed
//    @GetMapping
//    public ResponseEntity<FolderOpenApiFolderItemsInner> getAllFolderItemsByUserId(@RequestParam(name = "userId") Long userId) {
//        return folderService.getAllFolderItemsByUserId(userId);
//    }


    @Override
    public ResponseEntity<List<FolderItemOpenApi>> getAllFolderItemsByUserId(Long userId) {
        return ResponseEntity.ok(folderService.getAllFolderItemsByUserId(userId));
    }


}

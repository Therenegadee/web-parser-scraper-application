package parser.app.webscraper.controllers;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.app.webscraper.mappers.openapi.StorageMapper;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.api.StorageApiDelegate;
import parser.userService.openapi.model.StorageDTO;


@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController implements StorageApiDelegate {
    private final StorageService storageService;
    private final StorageMapper storageMapper;

    @Observed
    @PostMapping
    @Override
    public ResponseEntity<Void> createStorage(@RequestParam(name = "userId") Long userId) {
        return storageService.createStorage(userId);
    }

    @Observed
    @GetMapping("/{storageId}")
    @Override
    public ResponseEntity<StorageDTO> getStorageById(@PathVariable String storageId) {
        return ResponseEntity.ok(storageMapper.toDTO(storageService.findByStorageId(new ObjectId(storageId))));
    }

    @Observed
    @GetMapping
    @Override
    public ResponseEntity<StorageDTO> getStorageByUserId(@RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(storageMapper.toDTO(storageService.findByUserId(userId)));
    }


    @Observed
    @PutMapping
    @Override
    public ResponseEntity<Void> updateStorageByUserId(
            @RequestParam(name = "userId") Long userId,
            @RequestBody StorageDTO storageDTO
    ) {
        return storageService.updateStorageByUserId(userId, storageDTO);
    }

    @Observed
    @PutMapping("/{storageId}")
    @Override
    public ResponseEntity<Void> updateStorageById(
            @PathVariable(name = "storageId") String storageId,
            @RequestBody StorageDTO storageDTO
    ) {
        return storageService.updateStorageById(storageId, storageDTO);
    }
}

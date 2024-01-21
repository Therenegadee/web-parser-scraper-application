package parser.app.webscraper.services;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.StorageMapper;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.repository.StorageRepository;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.model.StorageOpenApi;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;

    @Observed
    @Override
    public StorageOpenApi findByStorageId(Long storageId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %d wasn't found", storageId)));
        return storageMapper.toOpenApi(storage);
    }

    @Observed
    @Override
    public StorageOpenApi findByUserId(Long userId) {
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with user id %d wasn't found", userId)));
        return storageMapper.toOpenApi(storage);
    }

    @Observed
    @Override
    public void updateStorageById(Long storageId, StorageOpenApi storageOpenApi) {
        Storage storage = storageMapper.toStorage(storageOpenApi);
        storageRepository.updateByStorageId(storageId, storage);
    }

    @Observed
    @Override
    public void updateStorageByUserId(Long userId, StorageOpenApi storageOpenApi) {
        Storage storage = storageMapper.toStorage(storageOpenApi);
        storageRepository.updateByUserId(userId, storage);
    }
}

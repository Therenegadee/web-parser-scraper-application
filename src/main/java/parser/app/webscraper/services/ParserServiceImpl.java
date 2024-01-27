package parser.app.webscraper.services;


import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parser.app.webscraper.exceptions.BadRequestException;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.ParserResultMapper;
import parser.app.webscraper.mappers.openapi.UserParserSettingsMapper;
import parser.app.webscraper.models.*;
import parser.app.webscraper.repository.StorageRepository;
import parser.app.webscraper.services.interfaces.ParserService;
import parser.userService.openapi.model.ParserResultOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Observed
@Service
@RequiredArgsConstructor
public class ParserServiceImpl implements ParserService {

    private final StorageRepository storageRepository;
    private final UserParserSettingsMapper parserSettingsMapper;
    private final ParserResultMapper parserResultMapper;

    @Observed
    @Override
    public ResponseEntity<Void> createParserSettings(Long userId, UserParserSettingsOpenApi userParserSettingsOpenApi) {
        UserParserSetting userParserSetting = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage of User With Id %d wasn't found", userId)));
        UUID parentId = userParserSetting.getParentFolderId();
        if(Objects.isNull(parentId)) {
            storage.addStorageItem(userParserSetting);
        } else {
            Folder parentFolder = storageRepository
                    .findFolderById(storage.getId(), parentId)
                    .orElseThrow(() -> new BadRequestException(String.format("Folder to put settings in with id %s (in storage with id: %s) wasn't found!", parentId, storage.getId())));
            parentFolder.addStorageItem(userParserSetting);
        }
        userParserSetting.setStorageId(storage.getId());
        storageRepository.updateByStorageId(storage.getId(), storage);
        return ResponseEntity
                .status(201)
                .build();
    }

//    @Observed
//    @Override
//    public ResponseEntity<Void> createParserSettings(
//            Long userId,
//            String folderName,
//            UserParserSettingsOpenApi userParserSettingsOpenApi
//    ) {
//        UserParserSetting userParserSetting = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
//        Storage storage = storageRepository
//                .findByUserId(userId)
//                .orElseThrow(() -> new NotFoundException(String.format("Storage of User With Id %d wasn't found", userId)));
//        storage.addStorageItemInsideFolder(userParserSetting, folderName);
//        storageRepository.updateByStorageId(storage.getId(), storage);
//        return ResponseEntity
//                .status(201)
//                .build();
//    }

    @Observed
    @Override
    public List<UserParserSettingsOpenApi> getAllParserSettingsByUserId(Long userId) {
        //todo: прописать поиск всех сеттингов в дефолтном методе репозитория
        List<UserParserSetting> userParserSetting = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with field user id = %d wasn't found", userId)))
                .getStorageItems()
                .stream()
                .filter(item -> item instanceof UserParserSetting)
                .map(item -> (UserParserSetting) item)
                .collect(Collectors.toList());
        return parserSettingsMapper.toOpenApi(userParserSetting);
    }

    @Observed
    @Override
    public UserParserSettingsOpenApi findParserSettingsById(UUID storageId, UUID settingsId) {
        return parserSettingsMapper.toOpenApi(
                storageRepository
                        .findParserSettingsById(storageId, settingsId)
                        .orElseThrow(() -> new NotFoundException(String.format("UserParserSetting with id %s in Storage (id: %s) wasn't found", settingsId, storageId)))
        );
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateParserSettingsById(
            UUID storageId,
            UUID settingsId,
            UserParserSettingsOpenApi userParserSettingsOpenApi
    ) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        List<StorageItem> storageItems = storage.getStorageItems();

        UserParserSetting sourceSetting = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        UserParserSetting targetSetting = storageRepository
                .findParserSettingsById(storageItems, settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        if (!targetSetting.getParentFolderId().equals(sourceSetting.getParentFolderId())) {
            Folder targetParentFolder = storageRepository
                    .findFolderById(storageItems, targetSetting.getParentFolderId())
                    .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", targetSetting.getParentFolderId(), storageId)));
            targetParentFolder.getStorageItems().remove(targetSetting);

            if (Objects.isNull(sourceSetting.getParentFolderId())) {
                storageItems.add(targetSetting);
            } else {
                Folder sourceParentFolder = storageRepository
                        .findFolderById(storageItems, sourceSetting.getParentFolderId())
                        .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", sourceSetting.getParentFolderId(), storageId)));
                sourceParentFolder.getStorageItems().add(targetSetting);
            }
        }
        targetSetting.setName(sourceSetting.getName());
        targetSetting.setTags(sourceSetting.getTags());
        targetSetting.setFirstPageUrl(sourceSetting.getFirstPageUrl());
        targetSetting.setNumOfPagesToParse(sourceSetting.getNumOfPagesToParse());
        targetSetting.setClassName(sourceSetting.getClassName());
        targetSetting.setTagName(sourceSetting.getTagName());
        targetSetting.setCssSelectorNextPage(sourceSetting.getCssSelectorNextPage());
        targetSetting.setHeader(sourceSetting.getHeader());
        targetSetting.setElementLocators(sourceSetting.getElementLocators());
        targetSetting.setParsingHistory(sourceSetting.getParsingHistory());
        targetSetting.setParentFolderId(sourceSetting.getParentFolderId());

        storageRepository.updateByStorageId(storageId, storage);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> deleteParserSettingsById(UUID storageId, UUID settingsId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        List<StorageItem> storageItems = storage.getStorageItems();

        UserParserSetting userParserSetting = storageRepository
                .findParserSettingsById(storageItems, settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        Folder parentFolder = storageRepository
                .findFolderById(storageItems, userParserSetting.getParentFolderId())
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", userParserSetting.getParentFolderId(), storageId)));

        parentFolder.getStorageItems().remove(userParserSetting);

        storageRepository.updateByStorageId(storageId, storage);
        return ResponseEntity
                .status(204)
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> runParser(UUID storageId, UUID settingsId, ParserResultOpenApi parserResultOpenApi) {
        ParserResult parserResult = parserResultMapper.toParserResult(parserResultOpenApi);
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        UserParserSetting userParserSetting = storageRepository
                .findParserSettingsById(storage.getStorageItems(), settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        //todo: прописать логику запуска парсера

        //TODO: добавить файл сервис и ссылку на скачивание

        parserResult.setLinkToDownloadResults("");
        userParserSetting.addParserResult(parserResult);
        storageRepository.updateByStorageId(storageId, storage);
        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    @Observed
    public ResponseEntity<Resource> downloadFile(UUID storageId, UUID settingsId, Long parserResultId) {
        return null;
    }

}

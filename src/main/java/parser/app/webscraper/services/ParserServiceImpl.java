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
        String parentId = userParserSetting.getParentFolderId();
        if(Objects.isNull(parentId)) {
            storage.addStorageItem(userParserSetting);
        } else {
            Folder parentFolder = storage.findFolderById(storage.getStorageItems(), parentId)
                    .orElseThrow(() -> new BadRequestException(String.format("Folder to put settings in with id %s (in storage with id: %s) wasn't found!", parentId, storage.getId())));
            parentFolder.addStorageItem(userParserSetting);
        }
        userParserSetting.setStorageId(storage.getId());
        storageRepository.save(storage);
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
    public UserParserSettingsOpenApi findParserSettingsById(String storageId, String settingsId) {
        return parserSettingsMapper.toOpenApi(
                storageRepository
                        .findParserSettingsById(storageId, settingsId)
                        .orElseThrow(() -> new NotFoundException(String.format("UserParserSetting with id %s in Storage (id: %s) wasn't found", settingsId, storageId)))
        );
    }

    @Observed
    @Override
    public ResponseEntity<Void> updateParserSettingsById(
            String storageId,
            String settingsId,
            UserParserSettingsOpenApi userParserSettingsOpenApi
    ) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));

        UserParserSetting sourceSetting = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        UserParserSetting targetSetting = storage.findParserSettingsById(storage.getStorageItems(), settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        if (!targetSetting.getParentFolderId().equals(sourceSetting.getParentFolderId())) {
            Folder targetParentFolder = storage
                    .findFolderById(storage.getStorageItems(), targetSetting.getParentFolderId())
                    .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", targetSetting.getParentFolderId(), storageId)));
            targetParentFolder.getStorageItems().remove(targetSetting);

            if (Objects.isNull(sourceSetting.getParentFolderId())) {
                storage.addStorageItem(targetSetting);
            } else {
                Folder sourceParentFolder = storage
                        .findFolderById(storage.getStorageItems(), sourceSetting.getParentFolderId())
                        .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", sourceSetting.getParentFolderId(), storageId)));
                sourceParentFolder.addStorageItem(targetSetting);
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

        storageRepository.save(storage);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> deleteParserSettingsById(String storageId, String settingsId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));

        UserParserSetting userParserSetting = storage
                .findParserSettingsById(storage.getStorageItems(), settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        Folder parentFolder = storage
                .findFolderById(storage.getStorageItems(), userParserSetting.getParentFolderId())
                .orElseThrow(() -> new NotFoundException(String.format("Folder with id %s in storage (id: %s) wasn't found", userParserSetting.getParentFolderId(), storageId)));

        parentFolder.getStorageItems().remove(userParserSetting);

        storageRepository.save(storage);
        return ResponseEntity
                .status(204)
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> runParser(String storageId, String settingsId, ParserResultOpenApi parserResultOpenApi) {
        ParserResult parserResult = parserResultMapper.toParserResult(parserResultOpenApi);
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        UserParserSetting userParserSetting = storage
                .findParserSettingsById(storage.getStorageItems(), settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        //todo: прописать логику запуска парсера

        //TODO: добавить файл сервис и ссылку на скачивание

        parserResult.setLinkToDownloadResults("");
        userParserSetting.addParserResult(parserResult);
        storageRepository.save(storage);
        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    @Observed
    public ResponseEntity<Resource> downloadFile(String storageId, String settingsId, Long parserResultId) {
        return null;
    }

}

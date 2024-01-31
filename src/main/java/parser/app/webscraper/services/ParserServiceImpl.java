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
import parser.app.webscraper.models.Folder;
import parser.app.webscraper.models.ParserResult;
import parser.app.webscraper.models.Storage;
import parser.app.webscraper.models.UserParserSetting;
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
        String parentId = userParserSettingsOpenApi.getParentFolderId();
        if (Objects.isNull(parentId)) {
            storage.addStorageItem(userParserSetting);
        } else {
            Folder parentFolder = storage
                    .findFolderById(parentId)
                    .orElseThrow(() -> new BadRequestException(String.format("Folder to put settings in with id %s (in storage with id: %s) wasn't found!", parentId, storage.getId())));
            userParserSetting.setParentFolderId(parentFolder.getId());
            parentFolder.addStorageItem(userParserSetting);
        }
        userParserSetting.setStorageId(storage.getId());
        storageRepository.save(storage);
        return ResponseEntity
                .status(201)
                .build();
    }

    @Observed
    @Override
    public List<UserParserSettingsOpenApi> getAllParserSettingsByUserId(Long userId) {
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage For User with id %s wasn't found", userId)));
        List<UserParserSetting> userParserSetting = storage.getStorageItems()
                .stream()
                .filter(item -> item instanceof UserParserSetting)
                .map(item -> (UserParserSetting) item)
                .collect(Collectors.toList());
        return parserSettingsMapper.toOpenApi(userParserSetting);
    }

    @Observed
    @Override
    public UserParserSettingsOpenApi findParserSettingsById(String storageId, String settingsId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        return parserSettingsMapper.toOpenApi(
                storage.findParserSettingsById(settingsId)
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
        UserParserSetting sourceSetting = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        UserParserSetting targetSetting = storage.findParserSettingsById(settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        if (!targetSetting.getParentFolderId().equals(userParserSettingsOpenApi.getParentFolderId())) {
            String parentFolderId = userParserSettingsOpenApi.getParentFolderId();
            if(Objects.nonNull(targetSetting.getParentFolderId())) {
                Folder exParentFolder = storage
                        .findFolderById(targetSetting.getParentFolderId())
                        .orElseThrow(() -> new BadRequestException(String.format("Parent Folder with id %s to put settings in doesn't exist", parentFolderId)));
                exParentFolder.getFolderItems().remove(targetSetting);
            } else {
                storage.getStorageItems().remove(targetSetting);
            }
            if(Objects.isNull(parentFolderId)) {
                storage.addStorageItem(targetSetting);
            } else {
                Folder parentFolder = storage
                        .findFolderById(parentFolderId)
                        .orElseThrow(() -> new BadRequestException(String.format("Parent Folder with id %s to put settings in doesn't exist", parentFolderId)));
                parentFolder.addStorageItem(targetSetting);
            }
            targetSetting.setParentFolderId(parentFolderId);
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

        userParserSettingsRepository.save(targetSetting);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> deleteParserSettingsById(String storageId, String settingsId) {
        UserParserSetting userParserSetting = userParserSettingsRepository
                .findByStorageIdAndId(storageId, settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));
        userParserSettingsRepository.delete(userParserSetting);
        return ResponseEntity
                .status(204)
                .build();
    }

    @Observed
    @Override
    public ResponseEntity<Void> runParser(String storageId, String settingsId, ParserResultOpenApi parserResultOpenApi) {
        ParserResult parserResult = parserResultMapper.toParserResult(parserResultOpenApi);
        UserParserSetting userParserSetting = userParserSettingsRepository
                .findByStorageIdAndId(storageId, settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        //todo: прописать логику запуска парсера

        //TODO: добавить файл сервис и ссылку на скачивание

        parserResult.setLinkToDownloadResults("");
        userParserSetting.addParserResult(parserResult);
        userParserSettingsRepository.save(userParserSetting);
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

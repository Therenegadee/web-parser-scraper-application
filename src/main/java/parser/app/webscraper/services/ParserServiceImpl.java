package parser.app.webscraper.services;


import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.exceptions.BadRequestException;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.ParserResultMapper;
import parser.app.webscraper.mappers.openapi.ParsingPresetMapper;
import parser.app.webscraper.models.*;
import parser.app.webscraper.repository.StorageRepository;
import parser.app.webscraper.services.interfaces.ParserService;
import parser.userService.openapi.model.ParserResultDTO;
import parser.userService.openapi.model.ParsingPresetDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Observed
@Service
@RequiredArgsConstructor
public class ParserServiceImpl implements ParserService {

    private final StorageRepository storageRepository;
    private final ParsingPresetMapper parserSettingsMapper;
    private final ParserResultMapper parserResultMapper;

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Void> createParserSettings(Long userId, ParsingPresetDTO parsingPresetDTO) {
        ParsingPreset parsingPreset = parserSettingsMapper.toUserParseSetting(parsingPresetDTO);
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage of User With Id %d wasn't found", userId)));
        String parentId = parsingPresetDTO.getParentFolderId();
        if (Objects.isNull(parentId)) {
            storage.addStorageItem(parsingPreset);
        } else {
            Folder parentFolder = storage
                    .findFolderById(parentId)
                    .orElseThrow(() -> new BadRequestException(String.format("Folder to put settings in with id %s (in storage with id: %s) wasn't found!", parentId, storage.getId())));
            parsingPreset.setParentFolderId(parentFolder.getId());
            parentFolder.addStorageItem(parsingPreset);
        }
        parsingPreset.setStorageId(storage.getId());
        storageRepository.save(storage);
        return ResponseEntity
                .status(201)
                .build();
    }

    @Observed
    @Transactional
    @Override
    public List<ParsingPresetDTO> getAllParserSettingsByUserId(Long userId) {
        Storage storage = storageRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage For User with id %s wasn't found", userId)));
        List<ParsingPreset> parsingPreset = storage.getStorageItems()
                .stream()
                .filter(item -> item instanceof ParsingPreset)
                .map(item -> (ParsingPreset) item)
                .collect(Collectors.toList());
        return parserSettingsMapper.toDTO(parsingPreset);
    }

    @Observed
    @Transactional
    @Override
    public ParsingPresetDTO findParserSettingsById(String storageId, String settingsId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        return parserSettingsMapper.toDTO(
                storage.findParserSettingsById(settingsId)
                        .orElseThrow(() -> new NotFoundException(String.format("ParsingPreset with id %s in Storage (id: %s) wasn't found", settingsId, storageId)))
        );
    }

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Void> updateParserSettingsById(
            String storageId,
            String settingsId,
            ParsingPresetDTO parsingPresetDTO
    ) {
        ParsingPreset sourceSetting = parserSettingsMapper.toUserParseSetting(parsingPresetDTO);
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        ParsingPreset targetSetting = storage.findParserSettingsById(settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        if (!targetSetting.getParentFolderId().equals(parsingPresetDTO.getParentFolderId())) {
            String parentFolderId = parsingPresetDTO.getParentFolderId();
            if (Objects.nonNull(targetSetting.getParentFolderId())) {
                Folder exParentFolder = storage
                        .findFolderById(targetSetting.getParentFolderId())
                        .orElseThrow(() -> new BadRequestException(String.format("Parent Folder with id %s to put settings in doesn't exist", parentFolderId)));
                exParentFolder.getFolderItems().remove(targetSetting);
            } else {
                storage.getStorageItems().remove(targetSetting);
            }
            if (Objects.isNull(parentFolderId)) {
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

        storageRepository.save(storage);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Void> deleteParserSettingsById(String storageId, String settingsId) {
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        StorageItem[] settingsWithFolder = storage
                .findSettingsWithParentFolder(settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));
        Folder parentFolder = (Folder) settingsWithFolder[0];
        parentFolder.getFolderItems().remove(settingsWithFolder[1]);
        return ResponseEntity
                .status(204)
                .build();
    }

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Void> runParser(String storageId, String settingsId, ParserResultDTO parserResultDTO) {
        ParserResult parserResult = parserResultMapper.toParserResult(parserResultDTO);
        Storage storage = storageRepository
                .findById(storageId)
                .orElseThrow(() -> new NotFoundException(String.format("Storage with id %s wasn't found", storageId)));
        ParsingPreset parsingPreset = storage
                .findParserSettingsById(settingsId)
                .orElseThrow(() -> new NotFoundException(String.format("Settings with id %s wasn't found", settingsId)));

        //todo: прописать логику запуска парсера

        //TODO: добавить файл сервис и ссылку на скачивание

        parserResult.setLinkToDownloadResults("");
        parsingPreset.addParserResult(parserResult);
        storageRepository.save(storage);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Resource> downloadFile(String storageId, String settingsId, Long parserResultId) {
        return null;
    }

}

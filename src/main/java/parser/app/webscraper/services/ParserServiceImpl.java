package parser.app.webscraper.services;


import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.ParserResultMapper;
import parser.app.webscraper.mappers.openapi.ParsingPresetMapper;
import parser.app.webscraper.models.*;
import parser.app.webscraper.services.interfaces.ParserService;
import parser.app.webscraper.services.interfaces.StorageService;
import parser.userService.openapi.model.ParserResultDTO;
import parser.userService.openapi.model.ParsingPresetDTO;

import java.util.Optional;

@Observed
@Service
@RequiredArgsConstructor
public class ParserServiceImpl implements ParserService {

    private final StorageService storageService;
    private final ParserResultMapper parserResultMapper;
    private final ParsingPresetMapper parsingPresetMapper;

    @Override
    public ResponseEntity<Void> createParserSettings(Long userId, ParsingPresetDTO parsingPresetDTO) {
        Storage storage = storageService.findByUserId(userId);
        ParsingPreset parsingPreset = parsingPresetMapper.toParsingPreset(parsingPresetDTO);
        Optional<Folder> parentFolderOptional = Optional.ofNullable(parsingPreset.getParentFolderId())
                .flatMap(storage::findFolderById);
        if (parentFolderOptional.isPresent()) {
            parentFolderOptional.get().addFolderItem(parsingPreset);
        } else {
            storage.addStorageItem(parsingPreset);
        }
        storageService.save(storage);
        return ResponseEntity.ok().build();
    }

    @Override
    public ParsingPresetDTO findParserSettingsById(ObjectId storageId, ObjectId presetId) {
        Storage storage = storageService.findByStorageId(storageId);
        return parsingPresetMapper.toDTO(storage.findParserSettingsById(presetId)
                .orElseThrow(() -> new NotFoundException(String.format("Parser Settings with id %s wasn't found", presetId))));
    }

    @Override
    public ResponseEntity<Void> deleteParserSettingsById(ObjectId storageId, ObjectId folderId, ObjectId presetId) {
        Storage storage = storageService.findByStorageId(storageId);
        storage.findFolderById(folderId)
                .orElseThrow(() -> new NotFoundException(String.format("Parser Settings with id %s wasn't found", presetId)))
                .getFolderItems()
                .removeIf(item -> item instanceof ParsingPreset && item.getId().equals(presetId));
        return null;
    }

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Void> runParser(ParsingPresetDTO parsingPresetDTO, ParserResultDTO parserResultDTO) {
        ParserResult parserResult = parserResultMapper.toParserResult(parserResultDTO);
        ParsingPreset parsingPreset = parsingPresetMapper.toParsingPreset(parsingPresetDTO);
        //todo: прописать логику запуска парсера

        //TODO: добавить файл сервис и ссылку на скачивание

        parserResult.setLinkToDownloadResults("");
        parsingPreset.addParserResult(parserResult);
        return ResponseEntity
                .ok()
                .build();
    }

    @Observed
    @Transactional
    @Override
    public ResponseEntity<Resource> downloadFile(ObjectId storageId, ObjectId presetId, Long parserResultId) {
        return null;
    }
}

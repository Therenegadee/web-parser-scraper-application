package parser.app.webscraper.services;



import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parser.app.webscraper.dao.interfaces.ParserResultDao;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.dao.interfaces.UserParserSettingsDao;
import parser.app.webscraper.mappers.openapi.ParserResultMapper;
import parser.app.webscraper.mappers.openapi.UserParserSettingsMapper;
import parser.app.webscraper.models.ParserResult;
import parser.app.webscraper.models.UserParserSetting;
import parser.app.webscraper.scraperlogic.ParserRunner;
import parser.app.webscraper.services.interfaces.ParserService;
import parser.userService.openapi.model.ParserResultOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j
public class ParserServiceImpl implements ParserService {
    private final ParserRunner parserRunner;
    private final UserParserSettingsMapper parserSettingsMapper;
    private final ParserResultMapper parserResultMapper;
    private final UserParserSettingsDao parseSettingRepository;
    private final ParserResultDao parserResultDao;

    @Override
    public Set<ParserResultOpenApi> getAllParserQueries() {
        return parserResultMapper.toOpenApi(parserResultDao.findAll());
    }

    @Override
    public ParserResultOpenApi showParserResultsById(Long id) {
        Optional<ParserResult> parserResultOpt = parserResultDao.findById(id);
        if (parserResultOpt.isPresent()) {
            return parserResultMapper.toOpenApi(parserResultOpt.get());
        }
        else {
            throw new NotFoundException(String.format("Parser results with id %d wasn't found", id));
        }
    }

    @Override
    public ResponseEntity<Void> setParserSettings(UserParserSettingsOpenApi userParserSettingsOpenApi) {
        UserParserSetting userParserSettings = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
        parseSettingRepository.save(userParserSettings);
        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    public ResponseEntity<Void> runParser(Long id) {
        Optional<UserParserSetting> userParserSettingOpt = parseSettingRepository.findById(id);
        UserParserSetting userParserSetting;
        if (userParserSettingOpt.isPresent()) {
            userParserSetting = userParserSettingOpt.get();
        } else {
//            log.debug("userParserSetting was empty, no such object in db");
            return ResponseEntity
                    .badRequest()
                    .build();
        }
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = user.getUsername();
//
//        Path path = Paths.get(parserRunner.runParser(userParserSetting, username));

        //TODO: добавить файл сервис

//        ParserResult parserResult = ParserResult
//                .builder()
//                .linkToDownloadResults(base64String)
//                .userParserSettings(parserSettingsMapper.toOpenApi(userParserSetting))
//                .build();
//        parserResultRepository.save(parserResult);
//        try {
//            Files.delete(path);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Long id) {
        return null;
    }
}

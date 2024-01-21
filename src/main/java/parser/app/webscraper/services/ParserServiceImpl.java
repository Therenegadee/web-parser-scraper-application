package parser.app.webscraper.services;


import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.mappers.openapi.ParserResultMapper;
import parser.app.webscraper.mappers.openapi.UserParserSettingsMapper;
import parser.app.webscraper.models.ParserResult;
import parser.app.webscraper.models.UserParserSetting;
import parser.app.webscraper.scraperlogic.ParserRunner;
import parser.app.webscraper.services.interfaces.ParserService;
import parser.userService.openapi.model.ParserResultOpenApi;
import parser.userService.openapi.model.UserParserSettingsOpenApi;

import java.util.List;
import java.util.Optional;

@Observed
@Service
@RequiredArgsConstructor
public class ParserServiceImpl  {

    // implements ParserService

//    private final ParserRunner parserRunner;
//    private final UserParserSettingsMapper parserSettingsMapper;
//    private final ParserResultMapper parserResultMapper;
//
//    @Observed
//    @Override
//    public ResponseEntity<Void> createParserSettings(Long userId, UserParserSettingsOpenApi userParserSettingsOpenApi) {
//        UserParserSetting userParserSetting = parserSettingsMapper.toUserParseSetting(userParserSettingsOpenApi);
//        userParserSetting.setUserId(userId);
//        userParserSettingsDao.save(userParserSetting);
//        return ResponseEntity
//                .status(201)
//                .build();
//    }
//
//    @Observed
//    @Override
//    public UserParserSettingsOpenApi getParserSettingsById(Long id) {
//        UserParserSetting userParserSetting = userParserSettingsDao.findById(id)
//                .orElseThrow(() -> new NotFoundException(String.format("UserParserSetting with id %d wasn't found")));
//        return parserSettingsMapper.toOpenApi(userParserSetting);
//    }
//
//    @Observed
//    @Override
//    public List<UserParserSettingsOpenApi> getAllParserSettingsByUserId(Long userId) {
//        List<UserParserSetting> userParserSetting = userParserSettingsDao.findAllByUserId(userId);;
//        return parserSettingsMapper.toOpenApi(userParserSetting);
//    }
//
//    @Observed
//    @Override
//    public ResponseEntity<Void> deleteParserSettingsById(Long id) {
//        userParserSettingsDao.deleteById(id);
//        return ResponseEntity
//                .status(204)
//                .build();
//    }
//
//    @Observed
//    @Override
//    public ResponseEntity<Void> runParser(Long id, ParserResultOpenApi parserResultOpenApi) {
//        ParserResult parserResult = parserResultMapper.toParserResult(parserResultOpenApi);
//        Optional<UserParserSetting> userParserSettingOpt = userParserSettingsDao.findById(id);
//        UserParserSetting userParserSetting;
//        if (userParserSettingOpt.isPresent()) {
//            userParserSetting = userParserSettingOpt.get();
//            userParserSetting.getParsingHistory().add(parserResult);
//            parserResult.setUserParserSetting(userParserSetting);
//            parserResultDao.save(parserResult);
//            //todo: прописать логику запуска парсера
//        } else {
////            log.debug("userParserSetting was empty, no such object in db");
//            return ResponseEntity
//                    .badRequest()
//                    .build();
//        }
////        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        String username = user.getUsername();
////
////        Path path = Paths.get(parserRunner.runParser(userParserSetting, username));
//
//        //TODO: добавить файл сервис
//
////        ParserResult parserResult = ParserResult
////                .builder()
////                .linkToDownloadResults(base64String)
////                .userParserSettings(parserSettingsMapper.toOpenApi(userParserSetting))
////                .build();
////        parserResultRepository.save(parserResult);
////        try {
////            Files.delete(path);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//        return ResponseEntity
//                .ok()
//                .build();
//    }
//
//    @Override
//    @Observed
//    public ResponseEntity<Resource> downloadFile(Long id) {
//        return null;
//    }
}

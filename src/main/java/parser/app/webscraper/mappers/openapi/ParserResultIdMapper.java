package parser.app.webscraper.mappers.openapi;

import lombok.RequiredArgsConstructor;
import parser.app.webscraper.dao.interfaces.ParserResultDao;
import parser.app.webscraper.exceptions.BadRequestException;
import parser.app.webscraper.exceptions.NotFoundException;
import parser.app.webscraper.models.ParserResult;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ParserResultIdMapper {
    private final ParserResultDao parserResultDao;

    public ParserResult getParserResultById(Long id) {
        return parserResultDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Parser Result with id %d wasn't found", id)));
    }

    public Set<ParserResult> getParserResultByIdS(List<Long> ids){
        return parserResultDao.findAllByIds(ids);
    }

    public Long getId(ParserResult parserResult) {
        if(parserResult == null){
            throw new BadRequestException("Object is null!");
        }
        return parserResult.getId();
    }
}

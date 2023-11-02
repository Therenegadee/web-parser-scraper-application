package parser.app.webscraper.dao.interfaces;

import org.springframework.stereotype.Repository;
import parser.app.webscraper.models.ElementLocator;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ElementLocatorDao {
    Optional<ElementLocator> findById(Long id);

    ElementLocator save(ElementLocator elementLocator);

    ElementLocator update(ElementLocator elementLocator);

    ElementLocator updateById(Long id, ElementLocator elementLocator);

    Set<ElementLocator> findAll();

    Set<ElementLocator> findAllByParserSettingsId(Long id);

    int deleteById(Long id);

    int delete(ElementLocator elementLocator);

    int deleteAll();

}

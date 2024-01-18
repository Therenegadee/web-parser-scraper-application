package parser.app.webscraper.dao.interfaces;

import org.springframework.stereotype.Repository;
import parser.app.webscraper.models.ElementLocator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ElementLocatorDao {
    Optional<ElementLocator> findById(Long id);

    Optional<ElementLocator> findByName(String name);

    List<ElementLocator> findAll();

    List<ElementLocator> findAllByParserSettingsId(Long id);

    ElementLocator save(ElementLocator elementLocator, Long settingsId);

    List<ElementLocator> saveAll(List<ElementLocator> elementLocator, Long settingsId);

    ElementLocator update(ElementLocator elementLocator);

    void updateAll(List<ElementLocator> elementLocator);

    ElementLocator updateById(Long id, ElementLocator elementLocator);

    int deleteById(Long id);

    int delete(ElementLocator elementLocator);

    int deleteAll();

}

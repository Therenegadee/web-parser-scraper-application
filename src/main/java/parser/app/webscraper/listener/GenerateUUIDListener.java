package parser.app.webscraper.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import parser.app.webscraper.models.Storage;

import java.util.UUID;

@Component
public class GenerateUUIDListener extends AbstractMongoEventListener<Storage> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Storage> event) {
        Storage storage = event.getSource();
        if (storage.isNew()) {
            storage.setId(UUID.randomUUID());
        }
    }

}

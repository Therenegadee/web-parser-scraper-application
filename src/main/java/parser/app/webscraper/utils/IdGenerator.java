package parser.app.webscraper.utils;

import org.springframework.stereotype.Service;
import parser.app.webscraper.models.StorageItem;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class IdGenerator {

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    public static String generateId(StorageItem storageItem) {
        StringBuilder idBuilder = new StringBuilder();

        Class<?> clazz = storageItem.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(storageItem);
                if (value != null) {
                    idBuilder.append(value.toString());
                }
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        idBuilder.append(counter.getAndIncrement());
        return idBuilder.toString();
    }
}

package parser.app.webscraper.mappers;

import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.Instant;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public class DateMapper {
    public Date toDate(OffsetDateTime offsetDateTime) {
        Instant instant = offsetDateTime.toInstant();
        return Date.from(instant);
    }

    public Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public OffsetDateTime toOffsetDateTime(Date date) {
        Instant instant = date.toInstant();
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}

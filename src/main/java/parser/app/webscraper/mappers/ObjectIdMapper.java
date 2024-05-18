package parser.app.webscraper.mappers;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class ObjectIdMapper {

    public String toId(ObjectId objectId) {
        return objectId.toHexString();
    }

    public ObjectId toObjectId(String id) {
        return new ObjectId(id);
    }
}
package adapters.json;

import com.fasterxml.jackson.core.JsonProcessingException;

import static adapters.json.ObjectMapperFactory.createTolerantObjectMapper;


public class JsonUtils {

    public static String jsonRepresentationOf(Object object) throws JsonProcessingException {
        return createTolerantObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static String jsonRepresentationOrBlowUpOf(Object object) {
        try {
            return createTolerantObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}

package de.intranda.counterscript.api;

import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.intranda.counterscript.model.MetadataInformation;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String jsonFromObject(Object object) {
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, object);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return null;
        }
        return writer.toString();
    }

    public static Object userFromJson(String json) {
        return objectFromJson(json, MetadataInformation.class);
    }

    static <t> MetadataInformation objectFromJson(String json, Class<t> klass) {
        MetadataInformation object;
        try {
            object = (MetadataInformation) mapper.readValue(json, klass);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return null;
        }
        return object;

    }

}

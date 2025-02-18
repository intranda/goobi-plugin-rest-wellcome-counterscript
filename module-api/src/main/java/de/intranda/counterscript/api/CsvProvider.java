package de.intranda.counterscript.api;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import de.intranda.counterscript.model.MetadataInformation;

@Provider
@Produces("text/csv")
public class CsvProvider implements MessageBodyWriter<List<MetadataInformation>> {
   
    @SuppressWarnings("rawtypes")
    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        boolean ret = List.class.isAssignableFrom(type);
        return ret;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public long getSize(List data, Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(List<MetadataInformation> data, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4,
            MultivaluedMap<String, Object> httpHeaders, OutputStream outputStream) throws IOException, WebApplicationException {
        
        httpHeaders.add("Content-Disposition", "attachment; filename=counterscript.csv");
        if (data != null && data.size() > 0) {
            CsvMapper mapper = new CsvMapper();
            Object o = data.get(0);
            CsvSchema schema = mapper.schemaFor(o.getClass()).withHeader();
            mapper.writer(schema).writeValue(outputStream, data);
        }

    }
}

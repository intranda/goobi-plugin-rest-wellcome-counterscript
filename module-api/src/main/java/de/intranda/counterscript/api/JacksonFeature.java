package de.intranda.counterscript.api;

import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;

import org.glassfish.jersey.CommonProperties;

import com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper;
import com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class JacksonFeature implements Feature {

    public boolean configure(final FeatureContext context) {

        final String disableMoxy =
                CommonProperties.MOXY_JSON_FEATURE_DISABLE + '.' + context.getConfiguration().getRuntimeType().name().toLowerCase();
        context.property(disableMoxy, true);

        context.register(JsonParseExceptionMapper.class);
        context.register(JsonMappingExceptionMapper.class);
        context.register(JsonUtils.class);
        context.register(JacksonJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
        return true;
    }
}

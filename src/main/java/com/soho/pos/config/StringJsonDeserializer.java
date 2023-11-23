package com.soho.pos.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 基于xss的JsonDeserializer
 */
public class StringJsonDeserializer extends JsonDeserializer<String> implements ContextualDeserializer {
    
    @Override
    public Class<String> handledType() {
        return String.class;
    }
    
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return StringUtils.trimToNull(jsonParser.getValueAsString());
    }
    
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        return this;
    }
}

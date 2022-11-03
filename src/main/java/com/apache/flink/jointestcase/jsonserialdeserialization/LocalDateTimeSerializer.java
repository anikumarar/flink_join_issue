package com.apache.flink.jointestcase.jsonserialdeserialization;

import java.io.IOException;
import java.time.LocalDateTime;

import com.apache.flink.jointestcase.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {


    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        // TODO Auto-generated method stub
        gen.writeString(DateUtil.convertDateToString(value));
    }

}
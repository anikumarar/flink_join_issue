package com.apache.flink.jointestcase.jsonserialdeserialization;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDatetimeDeserializer extends JsonDeserializer<LocalDateTime> {

	  @Override
	  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx)
	          throws IOException {
	      String str = p.getText();
	      try {
	    	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
	    	  return LocalDateTime.parse(str, formatter);
	      } catch (DateTimeParseException e) {
	          System.err.println(e);
	          return null;
	      }
	  }
}
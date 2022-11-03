package com.apache.flink.jointestcase.deserializationschema;



import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventSerializationSchema<T> implements KafkaSerializationSchema<T>{
	
	 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String topic;   
	    private ObjectMapper mapper;

	    public EventSerializationSchema(String topic) {
	        super();
	        this.topic = topic;
	    }
	    
	    
	    @Override
	    public ProducerRecord<byte[], byte[]> serialize(T obj, Long timestamp) {
	        byte[] b = null;
	        if (mapper == null) {
	            mapper = new ObjectMapper();
	        }
	         try {
	            b= mapper.writeValueAsBytes(obj);
	        } catch (JsonProcessingException e) {
	            // TODO 
	        }
	        return new ProducerRecord<byte[], byte[]>(topic, b);
	    }


}


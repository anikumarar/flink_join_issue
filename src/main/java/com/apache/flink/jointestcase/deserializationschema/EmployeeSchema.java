package com.apache.flink.jointestcase.deserializationschema;


import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.apache.flink.jointestcase.pojo.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;



public class EmployeeSchema  implements KafkaDeserializationSchema<Employee>{
	
	private static final long serialVersionUID = 1L;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public TypeInformation<Employee> getProducedType() {
		return TypeInformation.of(Employee.class);
	}

	public boolean isEndOfStream(Employee nextElement) {
		return false;
	}

	@Override
	public Employee deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
		Employee emp =  (Employee)this.objectMapper.readValue(record.value(), Employee.class);
		return emp;
	}

	
}

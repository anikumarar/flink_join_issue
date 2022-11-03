package com.apache.flink.jointestcase.deserializationschema;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.apache.flink.jointestcase.pojo.EmployeeExpense;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeExpenseSchema implements KafkaDeserializationSchema<EmployeeExpense>{
	
	private static final long serialVersionUID = 1L;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public TypeInformation<EmployeeExpense> getProducedType() {
		return TypeInformation.of(EmployeeExpense.class);
	}

	public boolean isEndOfStream(EmployeeExpense nextElement) {
		return false;
	}

	@Override
	public EmployeeExpense deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
		EmployeeExpense expense = (EmployeeExpense)this.objectMapper.readValue(record.value(), EmployeeExpense.class);
		return expense;
		
	}

}

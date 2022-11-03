package com.apache.flink.jointestcase;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apache.flink.jointestcase.deserializationschema.EmployeeExpenseSchema;
import com.apache.flink.jointestcase.deserializationschema.EmployeeSchema;
import com.apache.flink.jointestcase.deserializationschema.EventSerializationSchema;
import com.apache.flink.jointestcase.pojo.Employee;
import com.apache.flink.jointestcase.pojo.EmployeeCorrelatedWithSalaryAndExpense;
import com.apache.flink.jointestcase.pojo.EmployeeExpense;
import com.apache.flink.jointestcase.watermark.EDWBoundedOutOfOrdernessWatermarks;


public class Main {

	
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	static String TOPIC_ONE = "employee";
	static String TOPIC_TWO = "expense";
	static String TOPIC_OUT = "employeexpense";
	static String BOOTSTRAP_SERVER = "localhost:9092";
	static int ALLOWED_LATENCY_IN_SEC = 10;
	static int IDLENESS_IN_SEC = 5;
	static int WINDOW_SIZE_IN_SECONDS = 5; 

	
	public static void main(String[] args) {

		PushToKafka<String> p1 = new PushToKafka<String>(BOOTSTRAP_SERVER, StringSerializer.class.getName());

		PushToKafka<String> p2 = new PushToKafka<String>(BOOTSTRAP_SERVER, StringSerializer.class.getName());

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.setParallelism(1);

		Properties props = new Properties();
		props.put("bootstrap.servers", BOOTSTRAP_SERVER);
		props.put("client.id", "flink-example1");

		FlinkKafkaConsumer<Employee> kafkaConsumerOne = new FlinkKafkaConsumer<>(TOPIC_ONE, new EmployeeSchema(),props);
		
		
		LOG.info("Coming to main function");
		
	     kafkaConsumerOne.assignTimestampsAndWatermarks( new WatermarkStrategy<Employee>() {
            @Override
            public WatermarkGenerator<Employee> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new EDWBoundedOutOfOrdernessWatermarks<Employee>(Duration.ofSeconds(10)
						);
            }
        }
		    .withTimestampAssigner((employee, timestamp) -> employee.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
		    .withIdleness(Duration.ofSeconds(IDLENESS_IN_SEC))
		);
		
		

		kafkaConsumerOne.setStartFromLatest();

		FlinkKafkaConsumer<EmployeeExpense> kafkaConsumerTwo = new FlinkKafkaConsumer<>(TOPIC_TWO,
				new EmployeeExpenseSchema(), props);
		
		
		  kafkaConsumerTwo.assignTimestampsAndWatermarks( new WatermarkStrategy<EmployeeExpense>() {
		            @Override
		            public WatermarkGenerator<EmployeeExpense> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
		                return new EDWBoundedOutOfOrdernessWatermarks<EmployeeExpense>(Duration.ofSeconds(10)
								);
		            }
		        }
		  .withTimestampAssigner((employeeExpense, timestamp) -> employeeExpense.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
     	  .withIdleness(Duration.ofSeconds(IDLENESS_IN_SEC))
		);
  

		kafkaConsumerTwo.setStartFromLatest();


		EventSerializationSchema<EmployeeCorrelatedWithSalaryAndExpense> employeeWithExpenseSerializationSchema = new EventSerializationSchema<EmployeeCorrelatedWithSalaryAndExpense>(
				TOPIC_OUT);


		FlinkKafkaProducer<EmployeeCorrelatedWithSalaryAndExpense> sink = new FlinkKafkaProducer<EmployeeCorrelatedWithSalaryAndExpense>(TOPIC_OUT,
				employeeWithExpenseSerializationSchema, props, FlinkKafkaProducer.Semantic.AT_LEAST_ONCE);

		DataStream<Employee> empStream = env.addSource(kafkaConsumerOne)
				.keyBy(emps -> emps.getUuid());

		DataStream<EmployeeExpense> expStream = env.addSource(kafkaConsumerTwo).keyBy(exps -> exps.getUuid());


		empStream.join(expStream).where(new KeySelector<Employee, Tuple1<String>>() {
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple1<String> getKey(Employee value) throws Exception {
				return Tuple1.of(value.getUuid());

			}
		}).equalTo(new KeySelector<EmployeeExpense, Tuple1<String>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple1<String> getKey(EmployeeExpense value) throws Exception {
				return Tuple1.of(value.getUuid());
			}
		}).window(TumblingEventTimeWindows.of(Time.seconds(WINDOW_SIZE_IN_SECONDS)))
		.allowedLateness(Time.seconds(ALLOWED_LATENCY_IN_SEC))
				.apply(new JoinFunction<Employee, EmployeeExpense, EmployeeCorrelatedWithSalaryAndExpense>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					
					

					@Override
					public EmployeeCorrelatedWithSalaryAndExpense join(Employee first, EmployeeExpense second) throws Exception {
						return new EmployeeCorrelatedWithSalaryAndExpense(first.getTimestamp(), second.getTimestamp(),LocalDateTime.now(), second.getExpense(),first.getSalary(),
								first.getUuid());
					}

				}).addSink(sink);


		new MessagesGeneratorSameMilliSecond(p1, TOPIC_ONE, p2,TOPIC_TWO).start();


		try {
			env.execute();
			LOG.debug("Starting flink application!!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
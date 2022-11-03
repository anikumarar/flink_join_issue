package com.apache.flink.jointestcase;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.common.serialization.StringSerializer;

import com.apache.flink.jointestcase.pojo.Employee;

public class MessagesGeneratorSameMilliSecond extends Thread{
	Consumer c;
	Producer p;
	public MessagesGeneratorSameMilliSecond(PushToKafka<String> p1, String firstTopic,PushToKafka<String> p2, String secondTopic ) {
		BlockingQueue<Employee> bqueue = new ArrayBlockingQueue<Employee>(1000);
		c = new Consumer(bqueue,p1,secondTopic);
		p = new Producer(bqueue,p2,firstTopic);
	}
	
	@Override 
	public void run() {
	    Thread pThread = new Thread(c);
        Thread cThread = new Thread(p);
  
        // Start both threads
        pThread.start();
        cThread.start();
		
	}
	
	class Consumer implements Runnable  {
		 BlockingQueue<Employee> obj;
		 private  PushToKafka<String> p2;
		 private String secondTopic;
		 Random random = new Random();
	    public Consumer(BlockingQueue<Employee> obj,PushToKafka<String> p2, String secondTopic)
	    {
	        this.obj = obj;
	        this.p2  = p2;
	        this.secondTopic = secondTopic;
	    }
	  
	    @Override public void run()
	    {
	    	Random random = new Random();
	        while (true) {
	            try {
	                Employee emp = (Employee)obj.take();
//	                System.out.println("Consumed " + emp);
	                long timeRangeMs = 1000 * 2; // 1 seonds in ms
	                long randomTime = ThreadLocalRandom.current().nextLong(timeRangeMs);

					LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(
							LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() ),
							ZoneId.systemDefault());
					/***
					 * Adding between 0-3 seconds to simulate the behaviour that correlated record is 0-3 second appart	
					 */
					ldt = ldt.plusSeconds(Math.abs(random.nextInt(3)));

	                String creationTime = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
	                p2.send(secondTopic,
							"{" + "\"uuid\"" + ":" + "\"" + emp.getUuid() + "\"" + "," 
	                         + "\"timestamp\"" + ":"+ "\"" + creationTime + "\"" + "," 
							 + "\"expense\"" + ":" + "\""+ random.nextInt(20)+ "\"" + "}");
	            }
	            catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	class Producer implements Runnable {
		 BlockingQueue<Employee> obj;
		 private  PushToKafka<String> p1;
		 private String firstTopic;
		  
	    public Producer(BlockingQueue<Employee> obj,PushToKafka<String> p1, String firstTopic)
	    {
	        this.obj = obj;
	        this.p1  = p1;
	        this.firstTopic = firstTopic;
	    }
	  
	    @Override 
	    public void run()
	    {
	          
	    	Random rand = new Random();
	         // Produce numbers in the range [1,4]
	         // and put them in the buffer
	    	while (true) {
	            try {
	            	LocalDateTime creationTime = LocalDateTime.now();
	            	for (int i = 1; i <= 100; i++) {
	            		Employee emp = Employee.create(creationTime);
//	            		System.out.println("Producer " + emp);
	            		obj.put(emp);
	            		p1.send(firstTopic, "{" + "\"empName\"" + ":" + "\"" + emp.getEmpName() + "\"" + ","
								+ "\"timestamp\"" + ":" + "\"" + DateUtil.convertDateToString(emp.getTimestamp()) + "\"" + "," 
								+ "\"salary\"" + ":" + "\"" + emp.getSalary() + "\"" + "," 
	            				+ "\"uuid\"" + ":" + "\""+ emp.getUuid() + "\"" +  
								
	            				"}");
	          
	            	}
	          		Thread.sleep(1000);
	            }
	            catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	} 
	
	public static void main(String[] args) {
		
		 String TOPIC_ONE = "employee";
		 String TOPIC_TWO = "expense";
		 String BOOTSTRAP_SERVER = "localhost:9092";
//		 String TOPIC_ONE = "kafka_one_multi";
//		 String TOPIC_TWO = "kafka_two_multi";
//		 String BOOTSTRAP_SERVER = "localhost:9092";
		PushToKafka<String> p1 = new PushToKafka<String>(BOOTSTRAP_SERVER, StringSerializer.class.getName());

		PushToKafka<String> p2 = new PushToKafka<String>(BOOTSTRAP_SERVER, StringSerializer.class.getName());

		MessagesGeneratorSameMilliSecond messagesGeneratorSameMilliSecond = new MessagesGeneratorSameMilliSecond(p1,TOPIC_ONE,p2,TOPIC_TWO);
		
	    messagesGeneratorSameMilliSecond.start();
		
	}
}

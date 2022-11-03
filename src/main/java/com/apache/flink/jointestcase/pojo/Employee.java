package com.apache.flink.jointestcase.pojo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import com.apache.flink.jointestcase.DateUtil;
import com.apache.flink.jointestcase.jsonserialdeserialization.LocalDatetimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize
public class Employee  {

	public String empName;
	
	private Integer salary;
	
	/*Timestamp when expense was filed*/
	@JsonDeserialize(using = LocalDatetimeDeserializer.class)
//	@JsonSerialize(using = LocalDatetimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	public LocalDateTime timestamp;
	
	public String uuid;
	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public Employee() {
		super();
	}
	
	public Employee(LocalDateTime creationTime) {
		this.timestamp = creationTime;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(uuid, other.uuid);
	}
	
	


	public LocalDateTime getTimestamp() {
//		System.out.println("Printing ms "+this.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Employee [empName=" + empName +  ", timestamp=" + DateUtil.convertDateToString(timestamp) + ", uuid=" + uuid
				+ "]";
	}
	
	public static void main(String[] args) {
		final String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
	}

	final static String[] emp_names = { "john", "ted" };
	final static Random random = new Random();
	
	public static Employee create(LocalDateTime creationTime) {
		Employee emp = new Employee(creationTime);
		emp.setUuid(UUID.randomUUID().toString());
		emp.setEmpName(emp_names[random.nextInt(emp_names.length)]);
		emp.setSalary(random.nextInt(100));
		return emp;
	}

}

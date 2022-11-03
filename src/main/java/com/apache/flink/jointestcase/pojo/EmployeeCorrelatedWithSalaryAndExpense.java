package com.apache.flink.jointestcase.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.apache.flink.jointestcase.jsonserialdeserialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeCorrelatedWithSalaryAndExpense implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public LocalDateTime expense_timestamp;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public LocalDateTime employee_timestamp;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	/*Time of correlation of event*/
	private LocalDateTime correlation_time;

	@JsonProperty("expense")
	private Integer expense;

	@JsonProperty("uuid")
	private String uuid;
	
	@JsonProperty("salary")
	private Integer salary;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public EmployeeCorrelatedWithSalaryAndExpense() {
	}

	


	public Integer getExpense() {
		return expense;
	}

	public void setExpense(Integer expense) {
		this.expense = expense;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public EmployeeCorrelatedWithSalaryAndExpense(LocalDateTime employee_timestamp,LocalDateTime expense_timestamp,LocalDateTime correlation_time, Integer expense,Integer salary, String uuid) {
		super();
		this.employee_timestamp = employee_timestamp;
		this.expense_timestamp = expense_timestamp;
		this.correlation_time = correlation_time;
		this.expense = expense;
		this.uuid = uuid;
		this.salary = salary;
	}

	
	
	@Override
	public String toString() {
		return "EmployeeCorrelatedWithExpense [employee_timestamp=" + employee_timestamp + ", expense_timestamp=" + expense_timestamp +  ", expense=" + expense + ", uuid=" + uuid +", correlation_time=" + correlation_time +   "]";
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
		EmployeeCorrelatedWithSalaryAndExpense other = (EmployeeCorrelatedWithSalaryAndExpense) obj;
		return Objects.equals(uuid, other.uuid);
	}

}

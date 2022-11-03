package com.apache.flink.jointestcase.pojo;

import java.time.LocalDateTime;
import java.util.Objects;

import com.apache.flink.jointestcase.jsonserialdeserialization.LocalDatetimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonSerialize
public class EmployeeExpense {
	
	private String uuid;
	
	private Integer expense;
	

	
	@Override
	public String toString() {
		return "EmployeeExpense [uuid=" + uuid + ", expense=" + expense + ", timestamp=" + timestamp + "]";
	}



	

	public String getUuid() {
		return uuid;
	}





	public void setUuid(String uuid) {
		this.uuid = uuid;
	}





	public Integer getExpense() {
		return expense;
	}



	public void setExpense(Integer expense) {
		this.expense = expense;
	}



	public LocalDateTime getTimestamp() {
		return timestamp;
	}





	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}






	@JsonDeserialize(using = LocalDatetimeDeserializer.class)
//	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	public LocalDateTime timestamp;

	
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//	private Date currentTimestamp;
//	
	
	
//
//	public Date getCurrentTimestamp() {
//		return currentTimestamp;
//	}
//
//
//
//	public void setCurrentTimestamp(Date currentTimestamp) {
//		this.currentTimestamp = currentTimestamp;
//	}



	public EmployeeExpense() {
		super();
		// TODO Auto-generated constructor stub
	}



	@Override
	public int hashCode() {
		return Objects.hash(uuid, expense, timestamp);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeExpense other = (EmployeeExpense) obj;
		return Objects.equals(uuid, other.uuid) ;
	}



	public EmployeeExpense(String uuid, Integer expense, LocalDateTime timestamp) {
		super();
		this.uuid = uuid;
		this.expense = expense;
		this.timestamp = timestamp;
	}
	
	
	
	
	
	
	

}

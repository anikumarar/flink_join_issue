# flink_join_isssue

### Problem statement
This project attempts to test flink's tumbling window joining capabilities.


### Stream Description
We are using kafka for the data stream here. We have two input topics *employee* and *expense* and we are joining the two topics using uuid and producing the final result into *employeexpense* topic.

### Sample records

#### employee
{"empName":"ted","timestamp":"2022-11-03T14:14:13.659","salary":"50","uuid":"8a056e89-c365-454e-8b6e-bca4b2f74c40"}
{"empName":"ted","timestamp":"2022-11-03T14:14:13.659","salary":"61","uuid":"9ecb4341-2805-4f92-a94a-f21b379b7d07"}
{"empName":"john","timestamp":"2022-11-03T14:14:13.659","salary":"65","uuid":"18e96a63-638f-4419-8589-21304e00974d"}
{"empName":"john","timestamp":"2022-11-03T14:14:13.659","salary":"52","uuid":"31b8e35d-3d01-4aa5-89c8-0884fd2436f3"}

#### expense

{"uuid":"8a056e89-c365-454e-8b6e-bca4b2f74c40","timestamp":"2022-11-03T14:14:15.662","expense":"9"}
{"uuid":"9ecb4341-2805-4f92-a94a-f21b379b7d07","timestamp":"2022-11-03T14:14:13.738","expense":"9"}
{"uuid":"18e96a63-638f-4419-8589-21304e00974d","timestamp":"2022-11-03T14:14:14.840","expense":"3"}
{"uuid":"31b8e35d-3d01-4aa5-89c8-0884fd2436f3","timestamp":"2022-11-03T14:14:13.843","expense":"11"}

#### employeexpense

{"expense_timestamp":"2022-11-03T14:14:16.331","employee_timestamp":"2022-11-03T14:14:16.330","correlation_time":"2022-11-03T14:14:40.156","expense":15,"uuid":"3f7a7b4e-de8f-44d0-9570-0092e56381c3","salary":37}
{"expense_timestamp":"2022-11-03T14:14:17.503","employee_timestamp":"2022-11-03T14:14:17.460","correlation_time":"2022-11-03T14:14:40.202","expense":19,"uuid":"b725a5b4-e23b-4fec-bf0e-7bb1f392f848","salary":95}
{"expense_timestamp":"2022-11-03T14:14:18.655","employee_timestamp":"2022-11-03T14:14:18.571","correlation_time":"2022-11-03T14:14:40.202","expense":6,"uuid":"94429253-2109-4706-85f6-5af8c591bd8f","salary":29}
{"expense_timestamp":"2022-11-03T14:14:19.656","employee_timestamp":"2022-11-03T14:14:18.571","correlation_time":"2022-11-03T14:14:40.202","expense":10,"uuid":"cce60bab-24d2-466b-9ce1-dde8a0076b89","salary":18}

We have records out of order and the timestamps can repeat for multiple events. We have simulated the same using **MessagesGeneratorSameMilliSecond** class. We are creating the *employee* record and using the same current timestamp for 100 records and then keep those records into a BlockingQueue and then use the same records for the expense with randomly increasing the timstamp from 0 to 3 seconds.Idea here is to see what happens when different records have same event timestamp and they are out of order. Please check the code for further reference.

### Observations

We are seeing missing records for each window. We have tried to increase the bounded out of orderness parameters but its not helping. We brought the data from each of the topics and tried to join them and as you can see for every window interval we are missing records

TIME                |	COUNT_NOT_JOIN_REC(same window)	| COUNT_NOT_JOIN_REC(diff window)	| COUNT_JOIN_REC(same window) |	COUNT_JOIN_REC(diff window)
:---:               | :---:                           | :---:                           | :---:                       |:---:
2022-11-02 14:07:15	|0	                              |0	                              |102                          |0	
2022-11-02 14:07:20 |0	                              |98	                              |397	                        |0	
2022-11-02 14:07:25	|0	                              |103                              |397                          |0	
2022-11-02 14:07:30	|0	                              |103                              |411	                        |0	
2022-11-02 14:07:35	|0	                              |89	                              |407	                        |0	
2022-11-02 14:07:40	|0	                              |93	                              |296	                        |0	






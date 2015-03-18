## Introduction

VStriker it’s a Java desktop application that provides connectivity validation and estimates of CRUD (Create, Read, Update and Delete) operations throughput achievable against Object Stores (like ViPR® Data Services Object storage).  

VStriker's goal is to provide developers a quick and simple way of understanding the throughput  that can be expected from a single node client application connecting to ViPR Data Services that will be running in the same running in the same network conditions. 


![VStiker Throughput Analyzer](https://github.com/emccode/VStriker/blob/master/documentation/media/VStriker-Throughput-Analizer.png)

VStriker enables you to execute throughput analysis tests against the following REST APIs:

1. AWS S3
2. OpenStrack SWIFT
3. EMC Atmos

It provides the ability to fine tune the object workload to meet the expected user workload. It allows to configure the shape of the CRUD operations and the size of the Objects to be transferred. It also implements a multi-threading core that will sever to simulate a multiple connections to the Object Store. 



## Use Cases


The following section provides an overview of the Use Cases for the VStriker throughput analyzer. There is only one actor for this application, the end-user:

 
Use Case	             | Description
---------------------- | -----------|
Manage Account         | Accounts contain information about the target system and APIs/protocols to perform the throughput analysis against (CRUD Operations on Account)
Manage APIs/Protocols  | APIs/Protocol contains the information regarding the specific API and Protocol that is supported in the account. There are three main APIs and the protocols supported are HTTP and HTTPS (CRUD for  AWS S3, Swift, and Atmos)
Validate Account       | 	Validates the account's APIs. It tests that the accounts are properly configured. This validation includes the validation of all APIs/Protocols that are supported by the Account.
Configure Test Plan   | Test plans define the parameters, policies, and APIs/Protocols to be tested. There is a list of standard test and the ability for the use to customize it to their needs. VStriker also enables users to create their own throughput tests. 
Execute Test Plan      | Executes the selected test plan for the account and APIS selected.
View Test Results      |	View that provides the information resulting from the test performed.
View Report and Graphs	| Provides the user with a report of the test plan results and charts that compares the throughput of the CRUD operations performed per API/Protocol used.



## Architecture 

VStriker has a modular design. The following design shows the high level application architecture: 

VStriker-Architecture.png

![VStiker Throughput Analizer Architecture](https://github.com/emccode/VStriker/blob/master/documentation/media/VStriker-Architecture.png)

VStriker has been build in Java 1.8 and uses H2 database for persistence. The following has an overview of each one of the components

Component Name |	Component Description
-------------- | ---------------------|
JAVA UI |	UI for configuration, execution and display of the throughput tests. The UI has been build using Java FX. 
Accounts Library | 	Holds all the Accounts, APIs and Ports information
Test Plan Library |	Holds the test plain configuration information, pre-made plans and custom test plans
Test files Generator |	Generates tests files to be used as objects for the operations
Export Test to CSVs |	Exports the last test run results into a CSV file to be used for further analysis
Test Results Collector | 	Collects and processes (conversion calculations) of the data resulting from a throughput analysis. 
Test Engine |	Test engine uses the test plan information and performs the throughput analyses based parameters. The test engine divides the work into test units. Each one of those units is an operation (CRUD), which is executed in parallel based on the selected threading configuration.
Connectivity Validator	| Connectivity validator provides the user with confirmation that the connection information submitted for an API/Protocol is correct and it is properly configured.
Java S3 API	| Amazon’s Java S3 SDK Libraries
Java Swift API |	OpenStack’s Java Swift SDK Libraries
Java Atmos API | 	Atmos’ Java SDK Libraries
Test Data Store |	Data repository to store the test results, accounts and test plans
 
 

##Application Workflow 

The VStriker application work-flow is composed of four main steps: 


![VStiker Throughput Analyzer Architecture](https://github.com/emccode/VStriker/blob/master/documentation/media/VStriker-Application-Workflow.png)
 

**1.	Account Management:**  The application starts on the Account tab. This enables users to enter the target Object store information and APIs that it provides. Users enter the information regarding the APIs and endpoints configurations that need to be tested. Once the information is submitted, the user can then Validate the APIs. Once APIs (AWS S3, OpenStack Swift and EMC Atmos)  are validated, users can move to the next step.

**2.	Configuration Management:** The configuration management step enables users to select a test plan from a list of pre-built ones or to create a new one. The test plans provides a granular set of options to the user to map the test to their object workload.  

**3.	Test Execution:** This step takes the information defined in the session and the test plan configuration, it then generates the load to be applied to the defined endpoints and executes the test. This step also records the test execution information that will later be used for the user.

**4.	View Results:** This step provides a consolidated view of the test results. It provides raw data, averages, graphs and the ability to export the data for further analysis.

### Account Management 

Account Management enables users to organize the test plans and execution for each account that they create. An account can have multiple APIs that may to be tested as part of the throughput analysis test.  The following figure shows the relationship:

**[Add Image of account management]**

APIs in an account can be validated. This function is performed by the Validation Engine. It enables users to verify connectivity and functionality of the validate API in the Object store. API validation performs the following checks: 

1.	Check the connectivity to the Object Store.
2.	Check the ability to perform CRUD Object operations.
3.	Check the Ability to perform CRUD bucket operations.
   

### Test Plan Configuration

Test plan enables users to select a pre-created test plan and to create a new one if the choose so. The test plan defines the load that will be send to the Object Store. The configuration is granular to enable users to closely map their current workload to get a throughput analysis against the Object Store.


![VStriker Throughput Analyzer Architecture](https://github.com/emccode/VStriker/blob/master/documentation/media/VStriker-Test-Plan-Configuration.png)


Users can configure their test plan using the following configuration parameters: 

Parameter Name | Parameter Description
---------------| ---------------------|
Object Size (in Kb) | Size of  the Objects to be used in CRUD operations 
Number of Operations | Number of operations to be performed. Operations. For example a CRUD on an object counts as 4 operations.  
Number of Threads | Number of Threads VStriker will operate with. This enables the simulation of multiple clients performing the test.
Operations to be Tested | This section enables the user to select the type of operations to be tested and the weight (as percent of times) that those operations will be executed. This enables to map and fine tune the expected loads for the applications using the back-end system. For Example, if I wanted to tested throughput of Deletions, I would select Delete operations and put 100% weigh on it. VStriker will take care of creating the objects, and analyze the Delete operations. I can also select a combination of Create, Delete, Update and Read operations to simulate a mixed workload. 
Number of Retries | Number of times VStriker will Retry an operation before giving up.
Object Size Report Unit | Unit to be used when generating the report. Sizes can be selected as (Kb, Mb, GB)
Random size Objects Parameters | Clicking this check-box enables the use of random size objects. This setting will disable the Object Size field. 
Object Min. Size (in Kb.) | Enter the Minimum size of the objects to be used for the test
Object Max. Size (in Kb.) | Enter the Maximum size of the objects to be used in the test
Object Size Preference | This allows you to define the type of object size load you may have in your application. This enables to fine tune the test workload to your needs.
Protocols to be Tested | Select what API you want to use in this test. This is tied to the APIs that have been defined and validated in the selected Account. 

Users can use the parameters to define the workload they want to test and get an estimate of the throughput they could accomplish with one client (multiple clients if using multi-threading).


### Test Plan Execution 

Once the user has selected the Account and Test Plan, we are ready for the execution. The execution engine will create the object require for test and run the test.  

[Insert screen shoot about the Execution Page]



### View and Export Restuls

Upon the completion of the Test Plan execution, the results are displayed to the customer. This include summary of the data, raw data, graphs and the ability to export the raw data into Excel.

[To Do] 


## References

[1] Java
 
[2] H2 Database 

[3] Other Reference



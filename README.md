# WSO2 Distributed transaction counter for Synapse

[![Build Status](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fwso2.org%2Fjenkins%2Fview%2Fproducts%2Fjob%2Fproducts%2Fjob%2Fproduct-apim%2F)](<To be added>)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![stackoverflow](https://img.shields.io/badge/stackoverflow-wso2mi-orange)](https://stackoverflow.com/tags/wso2-am/)
[![stackoverflow](https://img.shields.io/badge/stackoverflow-wso2am-orange)](https://stackoverflow.com/tags/wso2-micro-integrator/)

---

This distributed transaction counter is designed as a universal transaction counter for all Synapse ESB based 
applications such as WSO2 APIM gateway, WSO2 MI and WSO2 ESB. It is designed to be highly scalable, customizable and 
fault-tolerant. 

Distributed transaction counter consists of two main high-level components.

1. Transaction counter
   * A plugin for Synapse engine that counts transactions and send reports to the transaction recording service.
2. Transaction recording service
   * A standalone service that receives transaction records from the transaction counters and store them in a database.

#### High Level Architecture
![distributed_architecture.png](docs%2Fdistributed_architecture.png)

## Project structure

```shell
integration-transaction-counter
├── counter
│      └── "Contains Java source code for transaction counter plugin"
├── dbscripts
│      └── "Contains database scripts for transaction recording service"
├── docs
│      └── "Contains documentation for transaction counter"
└── service
       └── "Contains Ballerina source code for transaction recording service"
```
Transaction recording service is written in [Ballerina](https://ballerina.io/); our own cloud native programming language.
## Getting Started
Currently, you can set up the transaction counter with WSO2 APIM gateway and WSO2 MI.
1. Run transaction recording service
2. Configure your MI/APIM gateways

For more info on running Transaction recording service read on 
[Ballerina code to cloud deployment](https://ballerina.io/learn/code-to-cloud-deployment/)

### Running transaction recording service using docker
It is advised to run the transaction recording service in a separate server in your internal network. You can either 
build the transaction recording service from the source or use the pre-built docker image.

   1. Install docker in your server.
   2. You need to have a MySQL database to store transaction records. Install MySQL in your server locally or use a 
      remote production ready MySQL server. 
   3. Create a `Config.toml` file for configurations. An example configuration file is given below. Let's assume you 
      have it in `/home/user/service/Config.toml`.
       
      ```toml
      [databaseConfig]
      host = "localhost"
      port = 3306
      user = "root"
      password = ""
      database = "TransactionCountDB"

      [serverConfig]
      port = 8080
      certFile = "./cert/serverpubliccert.crt"
      keyFile = "./cert/serverpvtkey.key"

      [[ballerina.auth.users]]
      username="admin"
      password="admin"
      scopes=["read","write"]

      [[ballerina.auth.users]]
      username="user"
      password="user"
      scopes=["read"]
      ```
   4. Run following command in terminal
      ```shell
      docker run -d -v /home/user/service/Config.toml:/home/ballerina/Config.toml -p 9090:9090 wso2inc/transaction-counting-service:v0.1.0-SNAPSHOT
      ```
      
### Configuring your MI nodes and APIM gateways
Transaction counter component is already included in the WSO2 API Manager and WSO2 Micro Integrator distributions. You 
just need to add necessory configurations to `deployment.toml` to enable it. (APIM -`<APIM_HOME>/repository/conf/deployment.toml` 
and MI - `<MI_HOME>/conf/deployment.toml` )

Sample configurations are given below.

 ```toml
 [integration.transaction_counter]
 enable = true
 server_id = "Gateway1"
 producer_counting_thread_pool_size = 10
 producer_scheduled_interval = 10
 max_transaction_count_per_record = 20
 min_transaction_count_per_record = 5
 record_queue_size = 1000
 publisher_scheduled_interval = 5
 publisher_max_batch_size = 100
 publisher_max_retries = 3
 store_impl = "org.wso2.integration.transaction.counter.store.TransactionRecordStoreImpl"
 service_url = "https://localhost:8080/transactions/records"
 service_username = "admin"
 service_password = "admin"
 ```
Transaction counter is consists of following components.

![counter_highlevel_arch.png](docs%2Fcounter_highlevel_arch.png)

You can configure these components using the following configurations.

* `enable` - Enable or disable transaction counter
* `server_id` - Unique ID for the server. This will be used to identify the server in the transaction recording service.
* `producer_counting_thread_pool_size` - Number of threads to be used for counting transactions. Synapse threads offload 
the counting task to these threads. If transaction count is equal  or greater than `max_transaction_count_per_record` a transaction
record will be created and add to the TransactionRecordQueue.
> If your server is running in a high load, you need to increase this value to avoid blocking Synapse threads. If your server is running in a low load, you can decrease this value to save resources.

* `producer_scheduled_interval` - A single thread is running in the transaction record producer in given intervals. 
This thread creates a transaction record and add it to the TransactionRecordQueue if the transaction count is greater than
or equal to `min_transaction_count_per_record`. Purpose of this thread is to create transaction records in a timely manner.
* `max_transaction_count_per_record` act as a threshold to max number of transactions you can lose in case of a server crash.
* `min_transaction_count_per_record` is used to avoid creating transaction records for low number of transactions.

> If your server is getting high number of transactions you need to have low values for `producer_scheduled_interval` and high value for `max_transaction_count_per_record`. If your server is getting low number of transactions you need to have high values for `producer_scheduled_interval` and low value for `max_transaction_count_per_record`.

* `record_queue_size` - Maximum number of transaction records that can be stored in the TransactionRecordQueue. If the queue is full, transaction records will be dropped.
* `publisher_scheduled_interval` - A single thread is running in the transaction record publisher in given intervals and send batch of transaction records to the transaction recording service.
* `publisher_max_batch_size` - Maximum number of transaction records that can be sent to the transaction recording service in a single request. This value should be less than or equal to `record_queue_size` and based on the network bandwidth.
* `publisher_max_retries` - Maximum number of retries to send transaction records to the transaction recording service. If the transaction recording service is down, transaction records will be stored back in the TransactionRecordQueue and will be sent to the transaction recording service when it is up.

> If your server is getting high number of transactions per second you need to have high `record_queue_size`, low value for `publisher_scheduled_interval` and high `publisher_max_batch_size`.

* `store_impl` - Transaction record store implementation class. Default implementation is `org.wso2.integration.transaction.counter.store.TransactionRecordStoreImpl` and it sends transaction records to the Transaction recording service.

>You can implement your own store to have a custom behavior by implementing `org.wso2.integration.transaction.counter.store.TransactionRecordStore` interface. As an example you can implement a store to send transaction records to a MessageQueue.

* `service_url` - Transaction recording service URL
* `service_username` - Transaction recording service username
* `service_password` - Transaction recording service password

> `service_username` and `service_password` are used to authenticate the transaction counter with the transaction recording service.

## Issues and support
Please report issues at [WSO2 Integration Transaction Counter Issues](https://github.com/wso2/integration-transaction-counter/issues).

## Contributing to the project

As an open source project, we welcome contributions from the community. To contribute to the project you can send a pull request to the master branch. The `master` branch holds the latest unreleased source code. Before sending a pull request please make sure that your changes are compatible with the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0).

## License

This project is licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).

---

(c) 2023, [WSO2 LLC](http://www.wso2.org/). All Rights Reserved.



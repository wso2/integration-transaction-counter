# Distributed transaction counter for Synapse (DTC)
Synapse distributed transaction counter is designed to count and record transactions of any synapse based 
application. Currently, it supports WSO2 API Manager Synapse gateway and WSO2 Micro Integrator.

### Deployment architecture
DTC consists of two main high-level components.

1. Transaction counter
   * Included in the WSO2 API Manager and WSO2 Micro Integrator distributions. This component counts transactions and
     send reports to the transaction recording service.
2. Transaction recording service
   * A standalone service that receives transaction records from the transaction counters and
     store them in a database.
   
![distributed_architecture.jpeg](misc%2Fdistributed_architecture.jpeg)

## Transaction counter
Transaction counter is a plugin for synapse written in Java that counts transactions and sends them to the transaction recording service.
* High level architecture is as follows.
![counter_highlevel_arch.jpeg](misc%2Fcounter_highlevel_arch.jpeg)

* Components of the transaction counter are as follows.
![counter_component_arch.jpeg](misc%2Fcounter_component_arch.jpeg)

## How to set up transaction counting for WSO2 API Manager or WSO2 Micro Integrator

1. Setup and run transaction recording service.
    * Please follow this [guide](https://github.com/wso2/integration-transaction-counter/blob/main/service/README.md#how-to-setup) 
      to setup and run the transaction recording service.
2. Enable and configure transaction counter in all your MI and APIM gateways.
    * Transaction counter component is already included in the WSO2 API Manager and WSO2 Micro Integrator distributions.
    * Add following configurations to `deployment.toml`.
    * APIM - `<APIM_HOME>/repository/conf/deployment.toml` | MI - `<MI_HOME>/conf/deployment.toml`
    * Following values are for testing purpose only. You need to change them according to your setup and server load.

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
   * Restart servers



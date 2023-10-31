/*
 *  Copyright (c) 2023, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.integration.transaction.counter;

public class TransactionCounterConstants {

    public static enum ServerType {
        GATEWAY, MI
    }

    public static final String IS_THERE_ASSOCIATED_INCOMING_REQUEST = "is_there_incoming_request";
    public static final String TRANSPORT_WS = "ws";
    public static final String TRANSPORT_WSS = "wss";

    public static final String SERVER_ID = "serverId";
    public static final String TRANSACTION_COUNT_STORE_CLASS = "transactionCountStoreClass";
    public static final String TRANSACTION_RECORD_QUEUE_SIZE = "transactionRecordQueueSize";
    public static final String PRODUCER_THREAD_POOL_SIZE = "producerThreadPoolSize";
    public static final String TRANSACTION_COUNT_RECORD_INTERVAL = "transactionCountRecordInterval";
    public static final String MAX_TRANSACTION_COUNT = "maxTransactionCount";
    public static final String MIN_TRANSACTION_COUNT = "minTransactionCount";
    public static final String CONSUMER_COMMIT_INTERVAL = "consumerCommitInterval";
    public static final String MAX_TRANSACTION_RECORDS_PER_COMMIT = "maxTransactionRecordsPerCommit";
    public static final String MAX_RETRY_COUNT = "maxRetryCount";
    public static final String TRANSACTION_COUNT_SERVICE = "transactionCountService";
    public static final String TRANSACTION_COUNT_SERVICE_USERNAME = "transactionCountServiceUsername";
    public static final String TRANSACTION_COUNT_SERVICE_PASSWORD = "transactionCountServicePassword";

    // APIM Gateway related constants
    public static final String APIM_CONFIG_CLASS = "org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder";
    public static final String GATEWAY_CONFIG_ROOT = "APIGateway.TransactionCounter";
    public static final String GATEWAY_PRODUCER_THREAD_POOL_SIZE = GATEWAY_CONFIG_ROOT +
            ".ProducerThreadPoolSize";
    public static final String GATEWAY_QUEUE_SIZE = GATEWAY_CONFIG_ROOT + ".QueueSize";
    public static final String GATEWAY_STORE_CLASS = GATEWAY_CONFIG_ROOT + ".StoreClass";
    public static final String GATEWAY_MAX_TRANSACTION_COUNT = GATEWAY_CONFIG_ROOT +
            ".MaxTransactionCount";
    public static final String GATEWAY_MIN_TRANSACTION_COUNT = GATEWAY_CONFIG_ROOT +
            ".MinTransactionCount";
    public static final String GATEWAY_RECORD_INTERVAL = GATEWAY_CONFIG_ROOT
            + ".ProducerScheduledInterval";
    public static final String GATEWAY_MAX_RETRY_COUNT = GATEWAY_CONFIG_ROOT + ".MaxRetryCount";
    public static final String GATEWAY_MAX_TRANSACTION_RECORDS_PER_COMMIT = GATEWAY_CONFIG_ROOT
            + ".MaxBatchSize";
    public static final String GATEWAY_CONSUMER_COMMIT_INTERVAL = GATEWAY_CONFIG_ROOT
            + ".PublisherScheduledInterval";
    public static final String GATEWAY_SERVER_ID = GATEWAY_CONFIG_ROOT + ".ServerID";
    public static final String GATEWAY_SERVICE = GATEWAY_CONFIG_ROOT + ".ServiceURL";
    public static final String GATEWAY_SERVICE_USERNAME = GATEWAY_CONFIG_ROOT
            + ".ServiceUsername";
    public static final String GATEWAY_SERVICE_PASSWORD = GATEWAY_CONFIG_ROOT
            + ".ServicePassword";

    // MI related constants
    public static final String MI_CONFIG_CLASS = "org.wso2.config.mapper.ConfigParser";
    public static final String MI_CONFIG_ROOT = "integration.transaction_counter";
    public static final String MI_SERVER_ID = MI_CONFIG_ROOT + ".server_id";
    public static final String MI_PRODUCER_THREAD_POOL_SIZE = MI_CONFIG_ROOT +
            ".producer_counting_thread_pool_size";
    public static final String MI_RECORD_INTERVAL = MI_CONFIG_ROOT
            + ".producer_scheduled_interval";
    public static final String MI_MAX_TRANSACTION_COUNT = MI_CONFIG_ROOT +
            ".max_transaction_count_per_record";
    public static final String MI_MIN_TRANSACTION_COUNT = MI_CONFIG_ROOT +
            ".min_transaction_count_per_record";
    public static final String MI_QUEUE_SIZE = MI_CONFIG_ROOT + ".record_queue_size";
    public static final String MI_CONSUMER_COMMIT_INTERVAL = MI_CONFIG_ROOT
            + ".publisher_scheduled_interval";
    public static final String MI_MAX_TRANSACTION_RECORDS_PER_COMMIT = MI_CONFIG_ROOT
            + ".publisher_max_batch_size";
    public static final String MI_MAX_RETRY_COUNT = MI_CONFIG_ROOT + ".publisher_max_retries";
    public static final String MI_STORE_CLASS = MI_CONFIG_ROOT + ".store_impl";
    public static final String MI_SERVICE = MI_CONFIG_ROOT + ".service_url";
    public static final String MI_SERVICE_USERNAME = MI_CONFIG_ROOT
            + ".service_username";
    public static final String MI_SERVICE_PASSWORD = MI_CONFIG_ROOT
            + ".service_password";
}

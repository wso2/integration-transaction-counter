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
}

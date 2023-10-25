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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.AbstractExtendedSynapseHandler;
import org.apache.synapse.MessageContext;
import org.wso2.integration.transaction.counter.consumer.TransactionRecordConsumer;
import org.wso2.integration.transaction.counter.exception.TransactionCounterConfigurationException;
import org.wso2.integration.transaction.counter.producer.TransactionRecordProducer;
import org.wso2.integration.transaction.counter.queue.TransactionRecordQueue;
import org.wso2.integration.transaction.counter.record.TransactionRecord;
import org.wso2.integration.transaction.counter.store.TransactionRecordStore;
import org.wso2.integration.transaction.counter.config.TransactionCounterConfig;

import java.lang.reflect.Constructor;

/**
 * This class is the starting point of the transaction counter. This class is responsible for initializing all the
 * components of the transaction counter and starting them. This class extends the Synapse handler interface and is
 * responsible for intercepting the Synapse message flow and passing the message context to the transaction counting
 * logic. This is registered in handlers.xml in case of APIM and service bus initializer in case of MI/ESB.
 * @author - Isuru Wijesiri
 * @version - 1.0.0
 */
public class TransactionCountHandler extends AbstractExtendedSynapseHandler {
    private static final Log LOG = LogFactory.getLog(TransactionCountHandler.class);
    private TransactionRecordQueue transactionRecordQueue;
    private TransactionRecordProducer transactionRecordProducer;
    private TransactionRecordConsumer transactionRecordConsumer;
    private TransactionRecordStore transactionCountStore;
    private static boolean enabled = false;

    public TransactionCountHandler() {

        // Initialize the config mechanism
        try {
            TransactionCounterConfig.init();
        } catch (TransactionCounterConfigurationException e) {
            LOG.error("Error while initializing Transaction Counter. Transaction counter will be disabled", e);
            return;
        }

        try {
            Class<?> clazz = Class.forName(
                    TransactionCounterConfig.getTransactionCountStoreClass()
            );
            Constructor<?> constructor = clazz.getConstructor();
            this.transactionCountStore = (TransactionRecordStore) constructor.newInstance();
        } catch (Exception e) {
            LOG.error("Error while initializing Transaction Counter. Transaction counter will be disabled", e);
            return;
        }

        this.transactionRecordProducer = TransactionRecordProducer.getInstance();
        this.transactionRecordConsumer = TransactionRecordConsumer.getInstance();
        this.transactionRecordQueue = TransactionRecordQueue.getInstance();

        TransactionRecord.init(
                TransactionCounterConfig.getServerID(),
                TransactionCounterConfig.getServerType().toString()
        );

        this.transactionRecordQueue.init(
                TransactionCounterConfig.getTransactionRecordQueueSize()
        );

        this.transactionRecordProducer.init(
                transactionRecordQueue,
                TransactionCounterConfig.getProducerThreadPoolSize(),
                TransactionCounterConfig.getMaxTransactionCount(),
                TransactionCounterConfig.getMinTransactionCount(),
                TransactionCounterConfig.getTransactionCountRecordInterval());

        this.transactionCountStore.init(
                TransactionCounterConfig.getTransactionCountService(),
                TransactionCounterConfig.getTransactionCountServiceUsername(),
                TransactionCounterConfig.getTransactionCountServicePassword()
        );

        this.transactionRecordConsumer.init(
                transactionCountStore,
                transactionRecordQueue,
                TransactionCounterConfig.getConsumerCommitInterval(),
                TransactionCounterConfig.getMaxRetryCount(),
                TransactionCounterConfig.getMaxTransactionRecordsPerCommit());

        enabled = true;
    }

    @Override
    public boolean handleRequestInFlow(MessageContext messageContext) {
        if(!enabled) {
            return true;
        }
        int tCount = TransactionCountingLogic.handleRequestInFlow(messageContext);
        if(tCount > 0) {
            this.transactionRecordProducer.addTransaction(tCount);
        }
        return true;
    }

    @Override
    public boolean handleRequestOutFlow(MessageContext messageContext) {
        if(!enabled) {
            return true;
        }
        int tCount = TransactionCountingLogic.handleRequestOutFlow(messageContext);
        if(tCount > 0) {
            this.transactionRecordProducer.addTransaction(tCount);
        }
        return true;
    }

    @Override
    public boolean handleResponseInFlow(MessageContext messageContext) {
        if(!enabled) {
            return true;
        }
        int tCount = TransactionCountingLogic.handleResponseInFlow(messageContext);
        if(tCount > 0) {
            this.transactionRecordProducer.addTransaction(tCount);
        }
        return true;
    }

    @Override
    public boolean handleResponseOutFlow(MessageContext messageContext) {
        if(!enabled) {
            return true;
        }
        int tCount = TransactionCountingLogic.handleResponseOutFlow(messageContext);
        if(tCount > 0) {
            this.transactionRecordProducer.addTransaction(tCount);
        }
        return true;
    }

    @Override
    public boolean handleServerInit() {
        // Nothing to implement
        return true;
    }

    @Override
    public boolean handleServerShutDown() {
        // Clen up resources
        transactionRecordProducer.shutdown();
        transactionRecordConsumer.shutdown();
        transactionRecordQueue.clenUp();
        transactionCountStore.clenUp();
        return true;
    }

    @Override
    public boolean handleArtifactDeployment(String s, String s1, String s2) {
        // Nothing to implement
        return true;
    }

    @Override
    public boolean handleArtifactUnDeployment(String s, String s1, String s2) {
        // Nothing to implement
        return true;
    }

    @Override
    public boolean handleError(MessageContext messageContext) {
        // Nothing to implement
        return true;
    }
}

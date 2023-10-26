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

package org.wso2.integration.transaction.counter.producer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.integration.transaction.counter.record.TransactionRecord;
import org.wso2.integration.transaction.counter.queue.TransactionRecordQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionRecordProducer {

    private static double MAX_TRANSACTION_COUNT;
    private static double MIN_TRANSACTION_COUNT;
    private static final Log LOG = LogFactory.getLog(TransactionRecordProducer.class);
    private static TransactionRecordProducer instance = null;
    private TransactionRecordQueue transactionRecordQueue;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final AtomicInteger transactionCount = new AtomicInteger(0);

    private TransactionRecordProducer() {}

    public static TransactionRecordProducer getInstance() {
        if(instance == null) {
            instance = new TransactionRecordProducer();
        }
        return instance;
    }

    public void init(TransactionRecordQueue transactionRecordQueue, int threadPoolSize, double maxTransactionCount,
            double minTransactionCount, int transactionCountRecordInterval) {

        MAX_TRANSACTION_COUNT = maxTransactionCount;
        MIN_TRANSACTION_COUNT = minTransactionCount;

        this.transactionRecordQueue = transactionRecordQueue;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(this::produceRecordScheduled, 0,
                transactionCountRecordInterval, TimeUnit.SECONDS);
    }

    public void addTransaction(int tCount) {
        executorService.execute(() -> this.produceRecord(tCount));
    }

    private void produceRecord(int tCount) {
        lock.lock();
        try {
            int count = transactionCount.addAndGet(tCount);
            if (count >= MAX_TRANSACTION_COUNT) {
                TransactionRecord transactionRecord = new TransactionRecord(transactionCount.get());
                transactionRecordQueue.add(transactionRecord);
                transactionCount.set(0);
            }
        } catch (Exception e) {
            LOG.error("Error while handling transaction count.", e);
        } finally {
            lock.unlock();
        }
    }

    private void produceRecordScheduled() {
        lock.lock();
        try {
            int transactionCountValue = transactionCount.get();
            if (transactionCountValue >= MIN_TRANSACTION_COUNT) {
                TransactionRecord transactionRecord = new TransactionRecord(transactionCountValue);
                transactionRecordQueue.add(transactionRecord);
                transactionCount.set(0);
            }
        } catch (Exception e) {
            LOG.error("Error while handling transaction count.", e);
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        scheduledExecutorService.shutdownNow();
        executorService.shutdownNow();
    }

}

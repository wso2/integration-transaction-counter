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

package org.wso2.integration.transaction.counter.queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.integration.transaction.counter.record.TransactionRecord;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class TransactionRecordQueue {

    private static TransactionRecordQueue instance = null;
    private static ArrayBlockingQueue<TransactionRecord> transactionRecordQueue;

    private TransactionRecordQueue() {}

    public static TransactionRecordQueue getInstance() {
        if(instance == null) {
            instance = new TransactionRecordQueue();
        }
        return instance;
    }

    public void init(int size) {
        transactionRecordQueue = new ArrayBlockingQueue<>(size);
    }
    
    public void add(TransactionRecord transactionRecord) {
        transactionRecordQueue.add(transactionRecord);
    }
    public void addAll(ArrayList<TransactionRecord> transactionRecordList) {
        transactionRecordQueue.addAll(transactionRecordList);
    }

    public TransactionRecord take() throws InterruptedException {
        return transactionRecordQueue.take();
    }

    public void drain(ArrayList<TransactionRecord> transactionRecordList, int maxRecords) {
        transactionRecordQueue.drainTo(transactionRecordList, maxRecords);
    }

    public void clenUp() {
        transactionRecordQueue.clear();
    }
}

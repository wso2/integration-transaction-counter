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

package org.wso2.integration.transaction.counter.config;

import org.wso2.integration.transaction.counter.TransactionCounterConstants;
import org.wso2.integration.transaction.counter.exception.TransactionCounterConfigurationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class APIMConfigFetcher implements ConfigFetcher {

    private static APIMConfigFetcher instance = null;
    private final static HashMap<String, Object> configMap = new HashMap<>();

    private APIMConfigFetcher() throws TransactionCounterConfigurationException {
        try {
            Class<?> configClass = Class.forName(TransactionCounterConstants.APIM_CONFIG_CLASS);

            Object serviceReferenceHolder = configClass.getMethod("getInstance").invoke(null);
            Object apiManagerConfigurationService = configClass.getMethod("getAPIManagerConfigurationService")
                    .invoke(serviceReferenceHolder);
            Object apiManagerConfiguration = apiManagerConfigurationService.getClass()
                    .getMethod("getAPIManagerConfiguration").invoke(apiManagerConfigurationService);
            Method getFirstProperty = apiManagerConfiguration.getClass().getMethod("getFirstProperty",
                    String.class);

            // Reading the config values
            String  temp;

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_SERVER_ID);
            String SERVER_ID = temp;

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_STORE_CLASS);
            String TRANSACTION_COUNT_STORE_CLASS = temp;

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_QUEUE_SIZE);
            Integer TRANSACTION_RECORD_QUEUE_SIZE = null;
            if (temp != null) {
                TRANSACTION_RECORD_QUEUE_SIZE = Integer.parseInt(temp);
            }

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_PRODUCER_THREAD_POOL_SIZE);
            Integer PRODUCER_THREAD_POOL_SIZE = null;
            if (temp != null) {
                PRODUCER_THREAD_POOL_SIZE = Integer.parseInt(temp);
            }

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_RECORD_INTERVAL);
            Integer TRANSACTION_COUNT_RECORD_INTERVAL = null;
            if (temp != null) {
                TRANSACTION_COUNT_RECORD_INTERVAL = Integer.parseInt(temp);
            }

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_MAX_TRANSACTION_COUNT);
            Double MAX_TRANSACTION_COUNT = null;
            if (temp != null) {
                MAX_TRANSACTION_COUNT = Double.parseDouble(temp);
            }

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_MIN_TRANSACTION_COUNT);
            Double MIN_TRANSACTION_COUNT = null;
            if (temp != null) {
                MIN_TRANSACTION_COUNT = Double.parseDouble(temp);
            }

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_CONSUMER_COMMIT_INTERVAL);
            Integer CONSUMER_COMMIT_INTERVAL = null;
            if (temp != null) {
                CONSUMER_COMMIT_INTERVAL = Integer.parseInt(temp);
            }

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_MAX_TRANSACTION_RECORDS_PER_COMMIT);
            Integer MAX_TRANSACTION_RECORDS_PER_COMMIT = null;
            if (temp != null) {
                MAX_TRANSACTION_RECORDS_PER_COMMIT = Integer.parseInt(temp);
            }

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_MAX_RETRY_COUNT);
            Integer MAX_RETRY_COUNT = null;
            if (temp != null) {
                MAX_RETRY_COUNT = Integer.parseInt(temp);
            }

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_SERVICE);
            String TRANSACTION_COUNT_SERVICE = temp;

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_SERVICE_USERNAME);
            String TRANSACTION_COUNT_SERVICE_USERNAME = temp;

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_SERVICE_PASSWORD);
            String TRANSACTION_COUNT_SERVICE_PASSWORD = temp;

            configMap.put(TransactionCounterConstants.SERVER_ID, SERVER_ID);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_STORE_CLASS, TRANSACTION_COUNT_STORE_CLASS);
            configMap.put(TransactionCounterConstants.TRANSACTION_RECORD_QUEUE_SIZE, TRANSACTION_RECORD_QUEUE_SIZE);
            configMap.put(TransactionCounterConstants.PRODUCER_THREAD_POOL_SIZE, PRODUCER_THREAD_POOL_SIZE);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_RECORD_INTERVAL , TRANSACTION_COUNT_RECORD_INTERVAL);
            configMap.put(TransactionCounterConstants.MAX_TRANSACTION_COUNT, MAX_TRANSACTION_COUNT);
            configMap.put(TransactionCounterConstants.MIN_TRANSACTION_COUNT, MIN_TRANSACTION_COUNT);
            configMap.put(TransactionCounterConstants.CONSUMER_COMMIT_INTERVAL, CONSUMER_COMMIT_INTERVAL);
            configMap.put(TransactionCounterConstants.MAX_TRANSACTION_RECORDS_PER_COMMIT, MAX_TRANSACTION_RECORDS_PER_COMMIT);
            configMap.put(TransactionCounterConstants.MAX_RETRY_COUNT, MAX_RETRY_COUNT);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_SERVICE, TRANSACTION_COUNT_SERVICE);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_SERVICE_USERNAME, TRANSACTION_COUNT_SERVICE_USERNAME);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_SERVICE_PASSWORD, TRANSACTION_COUNT_SERVICE_PASSWORD);

        } catch (ClassNotFoundException e) {
            // This error won't be thrown here because it is already checked in TransactionCountConfig
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new TransactionCounterConfigurationException();
        } catch (NumberFormatException | NullPointerException e) {
            throw new TransactionCounterConfigurationException("Error while reading the config values", e);
        }
    }

    public static ConfigFetcher getInstance() throws TransactionCounterConfigurationException {
        if (instance == null) {
            instance = new APIMConfigFetcher();
        }
        return instance;
    }

    @Override
    public String getConfigValue(String key) {
        if (configMap.get(key) == null) {
            return null;
        }
        return configMap.get(key).toString();
    }

}

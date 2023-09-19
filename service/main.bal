// Copyright (c) 2023 WSO2 LLC (http://www.wso2.org) All Rights Reserved.
//
// WSO2 LLC licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/http;
import ballerina/log;
import ballerina/sql;
import TransactionCountingService.models;
import TransactionCountingService.dao;

// Configurations
configurable models:DatabaseConfig databaseConfig = ?;
configurable models:ServerConfig serverConfig = ?;

listener http:Listener httpsListener = new (serverConfig.port,
    secureSocket = {
        key: {
            certFile: serverConfig.certFile,
            keyFile: serverConfig.keyFile
        }
    }
);

@http:ServiceConfig { 
    auth: [
        {
            fileUserStoreConfig: {},
            scopes: ["read"]
        }
    ]
}

service /transactions on httpsListener {
    private final dao:DAO dao;

    function init() returns error? {
            self.dao = check new (
                databaseConfig.host,
                databaseConfig.user,
                databaseConfig.password,
                databaseConfig.database,
                databaseConfig.port
            );
    }

    resource isolated function get count() returns models:TransactionCountDTO|error {
        return check self.dao.getTotalTransactionCount();
    }

    resource isolated function post query/count(@http:Payload models:dbQueryDTO? dbQuery) returns models:TransactionCountDTO|error {
        return check self.dao.getTransactionCount(dbQuery);
    }

    @http:ResourceConfig {
        auth: [
            {
               fileUserStoreConfig: {},
               scopes: ["write"]
            }
       ]
    }
    resource isolated function post records(@http:Payload models:TransactionRecord[] transactionRecords) returns http:Response|error { 
        log:printInfo(transactionRecords.toJsonString()); 
        sql:ExecutionResult[] _ = check self.dao.insertTransactionRecords(transactionRecords);
        return new http:Response();
    }
}

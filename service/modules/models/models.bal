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

public type TransactionRecord record {
    readonly string id;
    string host;
    string serverID;
    string serverType;
    int count;
    string recordedTime;
};

public type DatabaseConfig record {|
    string host;
    int port;
    string user;
    string password;
    string database;
|};

public type ServerConfig record {|
    int port;
    string certFile;
    string keyFile;
|};

public type dbQueryDTO record {|
    string? host?;
    string? serverID?;
    string? serverType?;
    string? startTime?;
    string? endTime?;
|};

public type TransactionCountDTO record {|
    decimal? count;
|};

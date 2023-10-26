# Transaction Counting Service

This repo contains the the ballerina implementation of transaction counting service that is used with the transaction counting feature of WSO2 APIM and MI.

## How it works

Transaction Counting Service is the collection point of all transaction records of all your APIM Gateway instances and MI instances. This service should run as a seperate service in your internal network. When you enable transaction counting in your APIM/MI instances they will create transaction records and push them to the transaction counting service via the given REST api.

## How to setup
1. Enable transaction counting feature in your Gateway/MI nodes.
2. Get a server cert for where you run transaction counting service. This part is a must as we use TLS. You can genarate self signed certificate as follows.

    ```
    openssl req -new -newkey rsa:2048 -sha256 -days 365 -nodes -x509 -keyout serverpvtkey.key -out serverpubliccert.crt
    ```
3. Add transaction counting service server cert to your APIM/MI trust stores. You can add a cert to APIM trust stire as follows.

    ```
    keytool -import -trustcacerts -alias servercert -file server.crt -keystore client-truststore.jks -storepass wso2carbon
    ```
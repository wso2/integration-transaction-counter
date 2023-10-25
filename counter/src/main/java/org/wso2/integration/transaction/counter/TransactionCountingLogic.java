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

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;


/**
 * This class contains the logic for counting transactions.
 * In case of changing the logic, this class should be modified, replaced or extended.
 * @author - Isuru Wijesiri
 * @version - 1.0.0
 */
public class TransactionCountingLogic {

    public static int handleRequestInFlow(MessageContext messageContext) {
        org.apache.axis2.context.MessageContext axis2MessageContext =
                ((Axis2MessageContext) messageContext).getAxis2MessageContext();

        // Setting this property to identify request-response pairs
        messageContext.setProperty(TransactionCounterConstants.IS_THERE_ASSOCIATED_INCOMING_REQUEST, true);

        // Counting message received via an open WebSocket
        String transport = axis2MessageContext.getIncomingTransportName();
        if (transport.equals(TransactionCounterConstants.TRANSPORT_WS) ||
                transport.equals(TransactionCounterConstants.TRANSPORT_WSS)){
            return 1;
        }
        return 0;
    }

    public static int handleRequestOutFlow(MessageContext messageContext) {
        Object isThereAnAssociatedIncomingRequest = messageContext.getProperty(
                TransactionCounterConstants.IS_THERE_ASSOCIATED_INCOMING_REQUEST);

        // Counting outgoing messages that are not related to any request-response pair
        if (isThereAnAssociatedIncomingRequest == null) {
            return 1;
        }
        return 0;
    }

    public static int handleResponseInFlow(MessageContext messageContext) {
        return 0;
    }

    public static int handleResponseOutFlow(MessageContext messageContext) {
        Object isThereAnAssociatedIncomingRequest = messageContext.getProperty(
                TransactionCounterConstants.IS_THERE_ASSOCIATED_INCOMING_REQUEST);

        // Counting request-response pairs
        if (isThereAnAssociatedIncomingRequest instanceof Boolean) {
            return 1;
        }
        return 0;
    }
}

/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.messaging.impl.ws;

import com.ericsson.apex.core.infrastructure.messaging.MessageListener;
import com.ericsson.apex.core.infrastructure.messaging.impl.ws.messageblock.RawMessageBlock;

/**
 * The listener interface for receiving webSocketMessage events. The class that is interested in processing a webSocketMessage event implements this interface,
 * and the object created with that class is registered with a component using the component's addWebSocketMessageListener method. When the webSocketMessage
 * event occurs, that object's appropriate method is invoked.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @param <MESSAGE> the generic type
 * @see RawMessageBlock
 */
public interface WebSocketMessageListener<MESSAGE> extends MessageListener<MESSAGE>, Runnable {

    /**
     * This method is called by the class with which this message listener has been registered.
     *
     * @param incomingData the data forwarded by the message reception class
     */
    void onMessage(RawMessageBlock incomingData);

    /**
     * Register a data forwarder to which messages coming in on the web socket will be forwarded.
     *
     * @param listener The listener to register
     */
    void registerDataForwarder(MessageListener<MESSAGE> listener);

    /**
     * Unregister a data forwarder that was previously registered on the web socket listener.
     *
     * @param listener The listener to unregister
     */
    void unRegisterDataForwarder(MessageListener<MESSAGE> listener);
}

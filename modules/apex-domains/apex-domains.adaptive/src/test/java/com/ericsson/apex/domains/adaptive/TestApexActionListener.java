/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.adaptive;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.apex.core.engine.engine.EnEventListener;
import com.ericsson.apex.core.engine.event.EnEvent;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;

/**
 * The listener interface for receiving testApexAction events. The class that is interested in processing a testApexAction event implements this interface, and
 * the object created with that class is registered with a component using the component's <code>addTestApexActionListener</code> method. When the
 * testApexAction event occurs, that object's appropriate method is invoked.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestApexActionListener implements EnEventListener {
    private List<EnEvent> resultEvents = new ArrayList<>();

    private final String id;

    /**
     * Instantiates a new test apex action listener.
     *
     * @param id the id
     */
    public TestApexActionListener(final String id) {
        this.id = id;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public EnEvent getResult() {
        while (resultEvents.isEmpty()) {
            ThreadUtilities.sleep(100);
        }
        return resultEvents.remove(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.apex.core.engine.engine.EnEventListener#onEnEvent(com.ericsson.apex.core.engine.event.EnEvent)
     */
    @Override
    public void onEnEvent(final EnEvent actionEvent) {
        try {
            Thread.sleep(100);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        if (actionEvent != null) {
            System.out.println("Action event from engine:" + actionEvent.getName());
            resultEvents.add(actionEvent);
        }
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }
}

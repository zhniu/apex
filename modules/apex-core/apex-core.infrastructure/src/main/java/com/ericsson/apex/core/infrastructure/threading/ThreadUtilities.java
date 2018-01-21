/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.threading;

/**
 * This class is a helper class for carrying out common threading tasks.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class ThreadUtilities {

    /**
     * Private constructor to prevent sub-classing of this class.
     */
    private ThreadUtilities() {
    }

    /**
     * Sleeps for the specified number of milliseconds, hiding interrupt handling.
     *
     * @param milliseconds the milliseconds
     * @return true, if successful
     */
    public static boolean sleep(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (final InterruptedException e) {
            return false;
        }

        return true;
    }
}

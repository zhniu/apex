/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.monitoring;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;

/**
 * This class is used to monitor context creates, deletes, gets, sets, locks and unlocks on context items in Apex context albums.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ContextMonitor {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ContextMonitor.class);

    /**
     * Monitor an initiation on a context item.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param value The value of the item
     */
    public void monitorInit(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final Object value) {
        LOGGER.trace(monitor("INIT", null, albumKey, schemaKey, name, value));
    }

    /**
     * Monitor an initiation on a context item.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param value The value of the item
     * @param userArtifactStack the keys of the artifacts using the context map at the moment
     */
    public void monitorInit(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final Object value,
            final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("INIT", userArtifactStack, albumKey, schemaKey, name, value));
    }

    /**
     * Monitor a deletion on a context item.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param value The value of the item
     * @param userArtifactStack the keys of the artifacts using the context map at the moment
     */
    public void monitorDelete(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final Object value,
            final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("DEL", userArtifactStack, albumKey, schemaKey, name, value));
    }

    /**
     * Monitor get on a context item.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param value The value of the item
     * @param userArtifactStack the keys of the artifacts using the context map at the moment
     */
    public void monitorGet(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final Object value,
            final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("GET", userArtifactStack, albumKey, schemaKey, name, value));
    }

    /**
     * Monitor set on a context item.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param value The value of the item
     * @param userArtifactStack the keys of the artifacts using the context map at the moment
     */
    public void monitorSet(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final Object value,
            final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("SET", userArtifactStack, albumKey, schemaKey, name, value));
    }

    /**
     * Monitor a read lock on a key.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param userArtifactStack the keys of the artifacts using the context map at the moment
     */
    public void monitorReadLock(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("READLOCK", userArtifactStack, albumKey, schemaKey, name, null));
    }

    /**
     * Monitor a write lock on a key.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param userArtifactStack the keys of the artifacts using the context map at the moment
     */
    public void monitorWriteLock(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("WRITELOCK", userArtifactStack, albumKey, schemaKey, name, null));
    }

    /**
     * Monitor a read unlock on a key.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param userArtifactStack the keys of the artifacts using the context map at the moment
     */
    public void monitorReadUnlock(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("READUNLOCK", userArtifactStack, albumKey, schemaKey, name, null));
    }

    /**
     * Monitor a write unlock on a key.
     *
     * @param albumKey The item album
     * @param schemaKey The item schema
     * @param name The name of the item
     * @param userArtifactStack the keys of the artifacts using the context map at the moment
     */
    public void monitorWriteUnlock(final AxArtifactKey albumKey, final AxArtifactKey schemaKey, final String name, final AxConcept[] userArtifactStack) {
        LOGGER.trace(monitor("WRITEUNLOCK", userArtifactStack, albumKey, schemaKey, name, null));
    }

    /**
     * Monitor the user artifact stack.
     *
     * @param preamble the preamble
     * @param userArtifactStack The user stack to print
     * @param albumKey the album key
     * @param schemaKey the schema key
     * @param name the name
     * @param value the value
     * @return the string
     */
    private String monitor(final String preamble, final AxConcept[] userArtifactStack, final AxArtifactKey albumKey, final AxArtifactKey schemaKey,
            final String name, final Object value) {
        StringBuilder builder = new StringBuilder();

        builder.append(preamble);
        builder.append(",[");

        if (userArtifactStack != null) {
            boolean first = true;
            for (AxConcept stackKey : userArtifactStack) {
                if (first) {
                    first = false;
                }
                else {
                    builder.append(',');
                }
                if (stackKey instanceof AxArtifactKey) {
                    builder.append(((AxArtifactKey) stackKey).getID());
                }
                else if (stackKey instanceof AxReferenceKey) {
                    builder.append(((AxReferenceKey) stackKey).getID());
                }
                else {
                    builder.append(stackKey.toString());
                }
            }
        }
        builder.append("],");

        builder.append(albumKey.getID());
        builder.append(',');
        builder.append(schemaKey.getID());
        builder.append(',');
        builder.append(name);

        if (value != null) {
            builder.append(',');
            builder.append(value.toString());
        }

        return builder.toString();
    }
}

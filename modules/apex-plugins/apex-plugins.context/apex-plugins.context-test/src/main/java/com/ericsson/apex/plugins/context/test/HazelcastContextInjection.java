/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.test;

import java.util.Map.Entry;
import java.util.Random;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.impl.distribution.DistributorFactory;
import com.ericsson.apex.context.parameters.ContextParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbums;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class HazelcastContextInjection {
    private static final int ONE_SECOND = 1000;
    private static final int SIXTY_SECONDS = 60;
    private static final int MAX_INT_10000 = 10000;

    /**
     * Default constructor is private to avoid subclassing.
     */
    private HazelcastContextInjection() {
    }

    /**
     * The main method.
     * 
     * @param args the arguments to the method
     * @throws ContextException exceptions thrown on context injection
     */
    public static void main(final String[] args) throws ContextException {
        // For convenience, I created model programmatically, it can of course be read in from a policy model
        AxContextModel contextModel = createContextModel();

        // The model must be registered in the model service.
        ModelService.registerModel(AxContextSchemas.class, contextModel.getSchemas());
        ModelService.registerModel(AxContextAlbums.class, contextModel.getAlbums());

        // Configure APex to use Hazelcast distribution and locking
        ContextParameters contextParameters = new ContextParameters();
        contextParameters.getDistributorParameters().setPluginClass("com.ericsson.apex.plugins.context.distribution.hazelcast.HazelcastContextDistributor");
        contextParameters.getLockManagerParameters().setPluginClass("com.ericsson.apex.plugins.context.locking.hazelcast.HazelcastLockManager");

        // Fire up our distribution
        final AxArtifactKey distributorKey = new AxArtifactKey("ApexDistributor", "0.0.1");
        Distributor contextDistributor = new DistributorFactory().getDistributor(distributorKey);
        contextDistributor.init(distributorKey);

        // Now, get a handle on the album
        ContextAlbum myContextAlbum = contextDistributor.createContextAlbum(new AxArtifactKey("LongContextAlbum", "0.0.1"));

        int jvmID =  new Random().nextInt(MAX_INT_10000);
        String myLongKey    = "MyLong_" + jvmID;
        String commonLongKey = "CommonLong";

        // Put the long value for this JVM into the map
        myContextAlbum.put(myLongKey, new Long(0L));

        // Put the common long value to be used across JVMS in the map if its not htere already
        myContextAlbum.lockForWriting(commonLongKey);
        if (myContextAlbum.get(commonLongKey) == null) {
            myContextAlbum.put(commonLongKey, new Long(0L));
        }
        myContextAlbum.unlockForWriting(commonLongKey);

        // Run for 60 seconds to show multi JVM
        for (int i = 0; i < SIXTY_SECONDS; i++) {
            System.out.println("JVM " + jvmID + " iteration " + i);

            for (Entry<String, Object> albumEntry: myContextAlbum.entrySet()) {
                myContextAlbum.lockForReading(albumEntry.getKey());
                System.out.println(albumEntry.getKey() + "-->" + albumEntry.getValue());
                myContextAlbum.unlockForReading(albumEntry.getKey());
            }
            System.out.println("old " + myLongKey + ": " + myContextAlbum.get(myLongKey));

            myContextAlbum.lockForReading(commonLongKey);
            System.out.println("old CommonLong: " + myContextAlbum.get(commonLongKey));
            myContextAlbum.unlockForReading(commonLongKey);

            Long myLong = (Long) myContextAlbum.get(myLongKey);
            myLong++;
            myContextAlbum.put(myLongKey, myLong);

            myContextAlbum.lockForWriting(commonLongKey);
            Long commonLong = (Long) myContextAlbum.get(commonLongKey);
            commonLong++;
            myContextAlbum.put(commonLongKey, commonLong);
            myContextAlbum.unlockForWriting(commonLongKey);

            System.out.println("new myLong: " + myContextAlbum.get(myLongKey));
            System.out.println("new commonLong: " + myContextAlbum.get(commonLongKey));

            try {
                Thread.sleep(ONE_SECOND);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        contextDistributor.clear();
    }

    /**
     * This method just creates a simple context model programatically.
     * @return a context model
     */
    public static AxContextModel createContextModel() {
        final AxContextSchema longSchema = new AxContextSchema(new AxArtifactKey("LongSchema", "0.0.1"), "Java", "java.lang.Long");

        final AxContextSchemas schemas = new AxContextSchemas(new AxArtifactKey("Schemas", "0.0.1"));
        schemas.getSchemasMap().put(longSchema.getKey(), longSchema);

        final AxContextAlbum longAlbumDefinition = new AxContextAlbum(new AxArtifactKey("LongContextAlbum", "0.0.1"), "APPLICATION", true, longSchema.getKey());

        final AxContextAlbums albums = new AxContextAlbums(new AxArtifactKey("context", "0.0.1"));
        albums.getAlbumsMap().put(longAlbumDefinition.getKey(), longAlbumDefinition);

        final AxKeyInformation keyInformation = new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1"));
        final AxContextModel contextModel = new AxContextModel(new AxArtifactKey("LongContextModel", "0.0.1"), schemas, albums, keyInformation);
        contextModel.setKeyInformation(keyInformation);
        keyInformation.generateKeyInfo(contextModel);

        return contextModel;
    }
}

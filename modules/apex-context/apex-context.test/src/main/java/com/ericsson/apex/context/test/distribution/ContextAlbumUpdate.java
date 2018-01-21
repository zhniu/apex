/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.test.distribution;

import java.io.IOException;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.impl.distribution.DistributorFactory;
import com.ericsson.apex.context.test.factory.TestContextAlbumFactory;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;
import com.ericsson.apex.model.utilities.comparison.KeyedMapComparer;
import com.ericsson.apex.model.utilities.comparison.KeyedMapDifference;

/**
 * The Class ContextAlbumUpdate is used to test Context Album updates.
 */
public class ContextAlbumUpdate {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ContextAlbumUpdate.class);

    /**
     * Test context album update.
     *
     * @throws ApexModelException the apex model exception
     * @throws IOException the IO exception
     * @throws ApexException the apex exception
     */
    public void testContextAlbumUpdate() throws ApexModelException, IOException, ApexException {
        LOGGER.debug("Running TestContextAlbumUpdate test . . .");

        final AxArtifactKey distributorKey = new AxArtifactKey("ApexDistributor", "0.0.1");
        final Distributor contextDistributor = new DistributorFactory().getDistributor(distributorKey);

        final AxContextModel longModel = TestContextAlbumFactory.createLongContextModel();
        contextDistributor.registerModel(longModel);

        final AxContextAlbum longAlbum1Def = longModel.getAlbums().get(new AxArtifactKey("LongContextAlbum1", "0.0.1"));
        final ContextAlbum longAlbum1 = contextDistributor.createContextAlbum(longAlbum1Def.getKey());
        assert (longAlbum1 != null);

        final AxContextAlbum longAlbum2Def = longModel.getAlbums().get(new AxArtifactKey("LongContextAlbum2", "0.0.1"));
        final ContextAlbum longAlbum2 = contextDistributor.createContextAlbum(longAlbum2Def.getKey());
        assert (longAlbum2 != null);

        // CHECKSTYLE:OFF: checkstyle:magicNumber
        longAlbum1.put("0", (long) 0);
        longAlbum1.put("1", (long) 1);
        longAlbum1.put("2", (long) 2);
        longAlbum1.put("3", (long) 3);

        final KeyedMapDifference<String, Object> result0 = new KeyedMapComparer<String, Object>().compareMaps(longAlbum1, longAlbum2);

        assert (0 == result0.getDifferentValues().size());
        assert (0 == result0.getIdenticalValues().size());
        assert (0 == result0.getRightOnly().size());
        assert (4 == result0.getLeftOnly().size());

        longAlbum2.putAll(longAlbum1);

        final KeyedMapDifference<String, Object> result1 = new KeyedMapComparer<String, Object>().compareMaps(longAlbum1, longAlbum2);

        assert (0 == result1.getDifferentValues().size());
        assert (4 == result1.getIdenticalValues().size());
        assert (0 == result1.getRightOnly().size());
        assert (0 == result1.getLeftOnly().size());

        longAlbum1.put("4", (long) 4);
        longAlbum2.put("5", (long) 5);
        longAlbum1.put("67", (long) 6);
        longAlbum2.put("67", (long) 7);

        final KeyedMapDifference<String, Object> result2 = new KeyedMapComparer<String, Object>().compareMaps(longAlbum1, longAlbum2);

        assert (1 == result2.getDifferentValues().size());
        assert (4 == result2.getIdenticalValues().size());
        assert (1 == result2.getRightOnly().size());
        assert (1 == result2.getLeftOnly().size());

        longAlbum1.remove("0");
        longAlbum2.remove("3");
        // CHECKSTYLE:ON: checkstyle:magicNumber

        final KeyedMapDifference<String, Object> result3 = new KeyedMapComparer<String, Object>().compareMaps(longAlbum1, longAlbum2);

        assert (1 == result3.getDifferentValues().size());
        assert (2 == result3.getIdenticalValues().size());
        assert (2 == result3.getRightOnly().size());
        assert (2 == result3.getLeftOnly().size());

        contextDistributor.clear();
    }
}

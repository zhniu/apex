/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.test.locking;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextAlbum;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.impl.distribution.DistributorFactory;
import com.ericsson.apex.context.test.factory.TestContextAlbumFactory;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;
import com.google.gson.Gson;

/**
 * The Class ConcurrentContextJVM tests concurrent use of context in a single JVM.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class ConcurrentContextJVM {
    // Logger for this class
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ConcurrentContextJVM.class);

    private static final int TEN_MILLISECONDS = 10;
    private static final int IPV4_ADDRESS_LENGTH = 4;

    /**
     * The Constructor.
     *
     * @param testType the test type
     * @param jvmNo the jvm no
     * @param threadCount the thread count
     * @param threadLoops the thread loops
     * @throws ApexException the apex exception
     */
    private ConcurrentContextJVM(final String testType, final int jvmNo, final int threadCount, final int threadLoops) throws ApexException {
        super();
        LOGGER.debug("starting JVMs and threads . . .");

        final AxArtifactKey distributorKey = new AxArtifactKey("ApexDistributor" + jvmNo, "0.0.1");
        final Distributor contextDistributor = new DistributorFactory().getDistributor(distributorKey);

        // @formatter:off
        final AxArtifactKey[] usedArtifactStackArray = {
                new AxArtifactKey("testC-top", "0.0.1"),
                new AxArtifactKey("testC-next", "0.0.1"),
                new AxArtifactKey("testC-bot", "0.0.1")
                };
        // @formatter:on

        final AxContextModel albumsModel = TestContextAlbumFactory.createMultiAlbumsContextModel();
        contextDistributor.registerModel(albumsModel);

        final ContextAlbum lTypeAlbum = contextDistributor.createContextAlbum(new AxArtifactKey("LTypeContextAlbum", "0.0.1"));
        assert (lTypeAlbum != null);
        lTypeAlbum.setUserArtifactStack(usedArtifactStackArray);

        final Thread[] threadArray = new Thread[threadCount];

        for (int t = 0; t < threadCount; t++) {
            threadArray[t] = new Thread(new ConcurrentContextThread(jvmNo, t, threadLoops));
            threadArray[t].setName(testType + ":ConcurrentContextThread_" + jvmNo + "_" + t);
            threadArray[t].start();
            LOGGER.debug("started thread " + threadArray[t].getName());
        }

        boolean allFinished;
        do {
            allFinished = true;
            for (int t = 0; t < threadCount; t++) {
                if (threadArray[t].isAlive()) {
                    allFinished = false;
                    try {
                        Thread.sleep(TEN_MILLISECONDS);
                    }
                    catch (final Exception e) {
                    }
                    break;
                }
            }
        }
        while (!allFinished);

        LOGGER.debug("threads finished, end value is {}", lTypeAlbum.get("testValue"));
        contextDistributor.clear();
    }

    /**
     * The main method.
     *
     * @param args the args
     * @throws Exception Any exception thrown by the test code
     */
    @SuppressWarnings("unchecked")
    public static void main(final String[] args) throws Exception {
        configure();

        System.out.println("JVM Arguments: " + Arrays.toString(args));
        // CHECKSTYLE:OFF: checkstyle:magicNumber

        // An even number of arguments greater than 3
        if (args.length < 4 || (args.length % 2 != 0)) {
            LOGGER.error("invalid arguments: " + Arrays.toString(args));
            LOGGER.error("usage: TestConcurrentContextJVM testType jvmNo threadCount threadLoops [parameterKey parameterJson].... ");
            return;
        }

        int jvmNo = -1;
        int threadCount = -1;
        int threadLoops = -1;

        try {
            jvmNo = Integer.parseInt(args[1]);
        }
        catch (final Exception e) {
            LOGGER.error("invalid argument jvmNo", e);
            return;
        }

        try {
            threadCount = Integer.parseInt(args[2]);
        }
        catch (final Exception e) {
            LOGGER.error("invalid argument threadCount", e);
            return;
        }

        try {
            threadLoops = Integer.parseInt(args[3]);
        }
        catch (final Exception e) {
            LOGGER.error("invalid argument threadLoops", e);
            return;
        }

        for (int p = 4; p < args.length - 1; p += 2) {
            @SuppressWarnings("rawtypes")
            final Class parametersClass = Class.forName(args[p]);
            final AbstractParameters parameters = (AbstractParameters) new Gson().fromJson(args[p + 1], parametersClass);
            ParameterService.registerParameters(parametersClass, parameters);
        }
        
        for (Entry<Class<?>, AbstractParameters> parameterEntry: ParameterService.getAll()) {
            LOGGER.info("Parameter class " + parameterEntry.getKey().getCanonicalName() + "=" + parameterEntry.getValue().toString());
        }
        
        try {
            new ConcurrentContextJVM(args[0], jvmNo, threadCount, threadLoops);
        }
        catch (final Exception e) {
            LOGGER.error("error running test in JVM", e);
            return;
        }
        // CHECKSTYLE:ON: checkstyle:magicNumber
    }

    /**
     * This method setus up any static configuration required by the JVM.
     * 
     * @throws Exception on configuration errors
     */
    public static void configure() throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("hazelcast.config", "src/test/resources/hazelcast/hazelcast.xml");

        // The JGroups IP address must be set to a real (not loopback) IP address for Infinispan to work. IN order to ensure that all
        // the JVMs in a test pick up the same IP address, this function sets te address to be the first non-loopback IPv4 address
        // on a host
        TreeSet<String> ipAddressSet = new TreeSet<String>();

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                // Look for real IPv4 Internet addresses
                if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == IPV4_ADDRESS_LENGTH) {
                    ipAddressSet.add(inetAddress.getHostAddress());
                }
            }
        }

        if (ipAddressSet.size() == 0) {
            throw new Exception("cound not find real IP address for test");
        }
        System.out.println("Setting jgroups.tcp.address to: " +  ipAddressSet.first());
        System.setProperty("jgroups.tcp.address", ipAddressSet.first());
    }
}

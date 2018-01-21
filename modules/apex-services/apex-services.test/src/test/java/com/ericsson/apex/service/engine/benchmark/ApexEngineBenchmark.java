/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.benchmark;

//CHECKSTYLE:OFF
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.impl.schema.java.JavaSchemaHelperParameters;
import com.ericsson.apex.context.parameters.SchemaParameters;
import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.enginemodel.concepts.AxEngineModel;
import com.ericsson.apex.plugins.context.schema.avro.AvroSchemaHelperParameters;
import com.ericsson.apex.plugins.executor.java.JavaExecutorParameters;
import com.ericsson.apex.plugins.executor.javascript.JavascriptExecutorParameters;
import com.ericsson.apex.plugins.executor.jruby.JrubyExecutorParameters;
import com.ericsson.apex.plugins.executor.jython.JythonExecutorParameters;
import com.ericsson.apex.plugins.executor.mvel.MVELExecutorParameters;
import com.ericsson.apex.plugins.executor.testdomain.model.EvalDomainModelFactory;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.runtime.EngineService;
import com.ericsson.apex.service.engine.runtime.EngineServiceEventInterface;
import com.ericsson.apex.service.engine.runtime.impl.EngineServiceImpl;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParameters;

/**
 * The Class ApexEngineBenchmark.
 *
 * @author John Keeney (john.keeney@ericsson.com)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ApexEngineBenchmark extends ApexBenchmarkUtil {
    //constants
    private static final long MAX_STOP_WAIT    = 5000; //5 sec
    private static final long MAX_START_WAIT   = 5000; //5 sec
    private static final long MAX_PERTEST_WAIT = 1000; //1 sec

    // Logger for this class
    private static final XLogger logger = XLoggerFactory.getXLogger(ApexEngineBenchmark.class);

    private AxArtifactKey engineServiceKey = new AxArtifactKey("Machine-1_process-1_engine-1", "0.0.0");
    private final String eventpackage = EvalDomainModelFactory.class.getPackage().getName()+".events";

    private String justOneLang = null;

    private int LOOPS = 2;

    public static void main(String[] args) throws Exception{
        ApexEngineBenchmark test = new ApexEngineBenchmark();
        test.LOOPS = 10000;

        if(args.length>=1){
            test.LOOPS = Integer.parseInt(args[0]);
        }
        if(args.length==2){
            try{
                test.justOneLang = args[1];
            }
            catch(Exception ignore){}
        }

        test.setUp();

        test.TestA_SingletonEngine();
        test.TestB_3ThreadEngine();
        test.TestC_10ThreadEngine();
        test.TestD_50ThreadEngine();
        test.TestE_100ThreadEngine();

        test.tearDown();
    }

    @Before
    public void setUp() throws Exception {

        logger.debug("Running TestApexEngine. . .");

        super.setUp(justOneLang);
    }

    private String slogan;

    @Test
    public void TestA_SingletonEngine() throws ApexException {
        TestListener ecalistener = new TestECAListener(new ConcurrentLinkedQueue<ApexEvent>());
        TestListener oodalistener = new TestOODAListener(new ConcurrentLinkedQueue<ApexEvent>());

        final int threads = 1;
        System.out.println("ECA: "+apexECAModelDesc);
        slogan = "ECA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running ECA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan,  apexECAModelString,threads,LOOPS,ecalistener));
        System.out.println("OODA: "+apexOODAModelDesc);
        slogan = "OODA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running OODA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan, apexOODAModelString,threads,LOOPS,oodalistener));
    }

    //@Test
    public void TestB_3ThreadEngine() throws ApexException {
        TestListener ecalistener = new TestECAListener(new ConcurrentLinkedQueue<ApexEvent>());
        TestListener oodalistener = new TestOODAListener(new ConcurrentLinkedQueue<ApexEvent>());

        final int threads = 3;
        System.out.println("ECA: "+apexECAModelDesc);
        slogan = "ECA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running ECA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan,  apexECAModelString,threads,LOOPS,ecalistener));
        System.out.println("OODA: "+apexOODAModelDesc);
        slogan = "OODA"+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running OODA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan, apexOODAModelString,threads,LOOPS,oodalistener));
    }

    //@Test
    public void TestC_10ThreadEngine() throws ApexException {
        TestListener ecalistener = new TestECAListener(new ConcurrentLinkedQueue<ApexEvent>());
        TestListener oodalistener = new TestOODAListener(new ConcurrentLinkedQueue<ApexEvent>());

        final int threads = 10;
        System.out.println("ECA: "+apexECAModelDesc);
        slogan = "ECA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running ECA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan,  apexECAModelString,threads,LOOPS,ecalistener));
        System.out.println("OODA: "+apexOODAModelDesc);
        slogan = "OODA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running OODA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan, apexOODAModelString,threads,LOOPS,oodalistener));
    }

    //@Test
    public void TestD_50ThreadEngine() throws ApexException {
        TestListener ecalistener = new TestECAListener(new ConcurrentLinkedQueue<ApexEvent>());
        TestListener oodalistener = new TestOODAListener(new ConcurrentLinkedQueue<ApexEvent>());

        final int threads = 50;
        System.out.println("ECA: "+apexECAModelDesc);
        slogan = "ECA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running ECA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan,  apexECAModelString,threads,LOOPS,ecalistener));
        System.out.println("OODA: "+apexOODAModelDesc);
        slogan = "OODA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running OODA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan, apexOODAModelString,threads,LOOPS,oodalistener));
    }

    //@Test
    public void TestE_100ThreadEngine() throws ApexException {
        TestListener ecalistener = new TestECAListener(new ConcurrentLinkedQueue<ApexEvent>());
        TestListener oodalistener = new TestOODAListener(new ConcurrentLinkedQueue<ApexEvent>());

        final int threads = 100;
        System.out.println("ECA: "+apexECAModelDesc);
        slogan = "ECA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running ECA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan,  apexECAModelString,threads,LOOPS,ecalistener));
        System.out.println("OODA: "+apexOODAModelDesc);
        slogan = "OODA"+"\t"+justOneLang+"\t"+threads+"\t"+LOOPS+"\t"+this.getClass().getSimpleName()+"\t";
        System.out.println(slogan+"Running OODA policy model on engine service with "+threads+" engines with "+LOOPS+" loops");
        assertTrue(TestApexEngine(slogan, apexOODAModelString,threads,LOOPS,oodalistener));
    }

    private  boolean TestApexEngine(final String slogan, final String policymodel, final int threads, final int runs, final TestListener listener) throws ApexException {
        EngineService service = null;

        final Date initStartTime = new Date();

        // create engine with 3 threads
        EngineServiceParameters parameters = new EngineServiceParameters();
        parameters.setInstanceCount(threads);
        parameters.setName(engineServiceKey.getName());
        parameters.setVersion(engineServiceKey.getVersion());
        parameters.setId(100);
        parameters.getEngineParameters().getExecutorParameterMap().put("MVEL",  new MVELExecutorParameters());
        service = EngineServiceImpl.create(parameters);
        
        EngineParameters engineparameters = new EngineParameters();
        engineparameters.getExecutorParameterMap().put("MVEL",        new MVELExecutorParameters());
        engineparameters.getExecutorParameterMap().put("JAVASCRIPT",  new JavascriptExecutorParameters());
        engineparameters.getExecutorParameterMap().put("JYTHON",      new JythonExecutorParameters());
        engineparameters.getExecutorParameterMap().put("JAVA",        new JavaExecutorParameters());
        engineparameters.getExecutorParameterMap().put("JRUBY",       new JrubyExecutorParameters());

        SchemaParameters scheamparameters = new SchemaParameters();
        scheamparameters.getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
        scheamparameters.getSchemaHelperParameterMap().put("Java", new JavaSchemaHelperParameters());

        service = EngineServiceImpl.create(parameters);
        service.registerActionListener("listener", listener);
        service.updateModel(parameters.getEngineKey(), policymodel, true);

        // start all engines on this engine service
         service.startAll();
        final long starttime = System.currentTimeMillis();
        while(!service.isStarted() && System.currentTimeMillis()-starttime < MAX_START_WAIT){
            ThreadUtilities.sleep(50);
        }
        if(!service.isStarted()){
            System.err.println("Apex Service "+service.getKey()+" failed to start after "+MAX_START_WAIT+" ms");
            return false;
        }

        final Date initEndTime = new Date();

        System.out.println("Engine init time (ms):\t"+(initEndTime.getTime()-initStartTime.getTime())+" Status: "+service.getState());

        ThreadUtilities.sleep(2000);

        final EngineServiceEventInterface engineServiceEventInterface = service.getEngineServiceEventInterface();


        Date testStartTime;
        Date firstTime = new Date();
        Queue<ApexEvent> inputEvents = new ConcurrentLinkedQueue<>();
        for(int i = 0; i<runs;i++){
            final ApexEvent event = new ApexEvent("Event0000", "0.0.1", eventpackage, "ApexEvaluationTest", "");
            event.put("TestTemperature", i);
            event.put("FirstEventTimestamp", firstTime.getTime());
            event.put("EventNumber", i);
            inputEvents.add(event);
        }
        for(int i = 0; i<runs;i++){
            // Send some events
            testStartTime = new Date();
            final ApexEvent event = inputEvents.poll();
            event.put("SentTimestamp", testStartTime.getTime());
            engineServiceEventInterface.sendEvent(event);
        }

        final long maxwait = runs * MAX_PERTEST_WAIT; //allow max time per test per run
        final long startwaittime = System.currentTimeMillis();
        do{
            ThreadUtilities.sleep(200);
        }while(listener.getOutQueue().size()<runs && System.currentTimeMillis()-startwaittime<maxwait);
        if(listener.getOutQueue().size()<runs){
            System.err.println("Apex Service "+service.getKey()+" failed to receive all events. Only received "+listener.getOutQueue().size()+" out of "+runs+" after "+maxwait+"ms");
            return false;
        }

        // Stop all engines on this engine service
        service.stop();
        final long stoptime = System.currentTimeMillis();
        while(!service.isStopped() && System.currentTimeMillis()-stoptime < MAX_STOP_WAIT){
            ThreadUtilities.sleep(200);
        }
        if(!service.isStopped()){
            System.err.println("Apex Service "+service.getKey()+" failed to stop after "+MAX_STOP_WAIT+" ms");
            return false;
        }


        ThreadUtilities.sleep(500);

        listener.printResults(slogan, runs);
        System.out.println();

        System.out.println("Engine Stats: ");
        for (AxArtifactKey engineKey : service.getEngineKeys()) {
            printEngineStats(engineKey,service.getStatus(engineKey));
        }
        System.out.println("\n");

        return true;
    }


    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private void printEngineStats(AxArtifactKey enginekey, String engineStatsXml) {
        System.out.print(enginekey.getName()+" Stats: ");
        ApexModelReader<AxEngineModel> reader;
        try {
            reader = new ApexModelReader<>(AxEngineModel.class);
            AxEngineModel enginestatsmodel = reader.read(new ByteArrayInputStream(engineStatsXml.getBytes("UTF-8")));
            System.out.println(enginestatsmodel.getStats().toString());
        } catch (ApexModelException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}

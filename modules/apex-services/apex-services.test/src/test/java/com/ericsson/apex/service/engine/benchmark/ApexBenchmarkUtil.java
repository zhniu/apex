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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.After;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelWriter;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxStateTree;
import com.ericsson.apex.model.policymodel.concepts.AxTask;
import com.ericsson.apex.plugins.executor.testdomain.model.EvalDomainModelFactory;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.runtime.ApexEventListener;

/**
 * The Class ApexBenchmarkUtil.
 *
 * @author John Keeney (john.keeney@ericsson.com)
 */
public class ApexBenchmarkUtil {
    // Logger for this class

    protected AxPolicyModel apexECAPolicyModel = null;
    protected AxPolicyModel apexOODAPolicyModel = null;
    protected String apexECAModelString;
    protected String apexOODAModelString;
    protected String apexECAModelDesc;
    protected String apexOODAModelDesc;

    public void setUp(String justOneLang) throws Exception {

        apexECAPolicyModel = new EvalDomainModelFactory(justOneLang).getECAPolicyModel();
        assertNotNull(apexECAPolicyModel);

        apexOODAPolicyModel = new EvalDomainModelFactory(justOneLang).getOODAPolicyModel();
        assertNotNull(apexOODAPolicyModel);

        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        new ApexModelWriter<AxPolicyModel>(AxPolicyModel.class).write(apexECAPolicyModel, baOutputStream);
        apexECAModelString = baOutputStream.toString();
        apexECAModelDesc = getModelStateTaskDescription(apexECAPolicyModel);
        baOutputStream.reset();
        new ApexModelWriter<AxPolicyModel>(AxPolicyModel.class).write(apexOODAPolicyModel, baOutputStream);
        apexOODAModelString = baOutputStream.toString();
        apexOODAModelDesc = getModelStateTaskDescription(apexOODAPolicyModel);

    }


    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
    }

    protected String getModelStateTaskDescription(AxPolicyModel model){
        String desc = model.getKey().getName()+":";
        Map<AxArtifactKey, AxPolicy> policies = model.getPolicies().getPolicyMap();
        desc += policies.size()+" Policies: " + policies.keySet() +"\n";
        for (AxPolicy policy : policies.values()) {
            Map<String, AxState> states = policy.getStateMap();
            desc+= "  Policy: "+policy.getKey().getName()+ "States: "+states.keySet();

            AxStateTree stateTree = policy.getStateTree();
            for (AxState state : stateTree.getReferencedStateSet()) {
                desc+= "\n    State: \""+state.getKey().getLocalName()+"\"";
                desc += "  ::  TSL: "+(state.getTaskSelectionLogic()!=null?state.getTaskSelectionLogic().getLogicFlavour():"NONE");
                Set<AxArtifactKey> tasks = state.getTaskReferences().keySet();
                desc += "   ::    TASKS("+tasks.size()+"): [";
                for (AxArtifactKey tk : tasks) {
                    AxTask task = model.getTasks().getTaskMap().get(tk.getKey());
                    desc += (state.getDefaultTask().getName().equals(task.getKey().getName())?"*":" ")+task.getKey().getName()+"("+task.getTaskLogic().getLogicFlavour()+")";
                }
                desc+="]";
            }
        }
        return desc+"\n";
    }

    /**
     * The listener interface for receiving test events. The class that is interested in processing a test event implements this interface, and the object
     * created with that class is registered with a component using the component's <code>addTestListener<code> method. When
     * the test event occurs, that object's appropriate
     * method is invoked.
     *
     * @see TestEvent
     */
    protected abstract class TestListener implements ApexEventListener {

        final protected Queue<ApexEvent> outputEvents;
        TestListener(Queue<ApexEvent> outputEvents){
            this.outputEvents = outputEvents;
        }
        /*
         * (non-Javadoc)
         *
         * @see com.ericsson.apex.service.engine.runtime.ApexEventListener#onApexEvent(com.ericsson.apex.service.engine.event.ApexEvent)
         */
        @Override
        final public synchronized void onApexEvent(ApexEvent event) {
            //checkResult(event);

            final Date testEndTime = new Date();

            event.put("RecvdTimestamp", testEndTime.getTime());

            if(outputEvents!=null){
                outputEvents.add(event);
            }

            checkResult(event);
        }

        public synchronized void printResults(final String slogan, final int count) {
            System.out.println(outputEvents.size()+ "\tevents received");
            if(outputEvents.size() != count){
                System.err.println("Why have we received "+outputEvents.size()+ " events when we were expecting "+count+"????");
            }
            long maxtime = -1;
            //double sumtime = 0;
            //double powersum = 0;
            final double cnt = (double)count;
            while(!outputEvents.isEmpty()){
                final ApexEvent event = outputEvents.poll();
                //final Date testStartTime = new Date((Long) event.get("SentTimestamp"));
                final Date testEndTime = new Date((Long) event.get("RecvdTimestamp"));
                //final long eventnum = (Long) event.get("EventNumber");
                final Date firstEventTimestamp = new Date((Long) event.get("FirstEventTimestamp"));
                long fulltime = testEndTime.getTime() - firstEventTimestamp.getTime();
                //long time = testEndTime.getTime() - testStartTime.getTime();
                if(maxtime<fulltime)
                    maxtime = fulltime;
                //sumtime += time;
                //powersum += (time*time);
                //System.out.print("\tEvent ("+eventnum+") execution time:\t" + time + "\tms ");
                //System.out.println("Output Event: " + event);
            }
            //final double avg = (sumtime/count);
            //final double variance = ((cnt*powersum) - (sumtime*sumtime))/(cnt*cnt);
            //final double stdev = Math.sqrt(variance);
            final double thruput = 1000.0 * cnt/(double)maxtime;
            System.out.println(slogan+count+ "\tevents received. e2e time (ms):\t "+maxtime+"\t thruput (/sec):\t "+thruput);
        }
        protected final Queue<ApexEvent> getOutQueue(){
            return outputEvents;
        }

        abstract void checkResult(ApexEvent result);
    }

    protected class TestOODAListener extends TestListener {

        TestOODAListener(Queue<ApexEvent> outputEvents) {
            super(outputEvents);
        }

        /**
         * Check result.
         *
         * @param result the result
         */
        void checkResult(ApexEvent event) {
            assertTrue(event.getName().startsWith("Event0004"));
        }
    }
    protected class TestECAListener extends TestListener {

        TestECAListener(Queue<ApexEvent> outputEvents) {
            super(outputEvents);
        }

        /**
         * Check result.
         *
         * @param result the result
         */
        void checkResult(ApexEvent event) {
            assertTrue(event.getName().startsWith("Event0003"));
        }
    }
}

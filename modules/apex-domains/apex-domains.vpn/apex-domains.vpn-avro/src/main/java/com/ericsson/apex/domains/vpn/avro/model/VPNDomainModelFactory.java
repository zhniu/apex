/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.vpn.avro.model;

import java.util.Set;
import java.util.TreeSet;

import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbums;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxEvents;
import com.ericsson.apex.model.eventmodel.concepts.AxField;
import com.ericsson.apex.model.policymodel.concepts.AxLogicReader;
import com.ericsson.apex.model.policymodel.concepts.AxPolicies;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.policymodel.concepts.AxState;
import com.ericsson.apex.model.policymodel.concepts.AxStateOutput;
import com.ericsson.apex.model.policymodel.concepts.AxStateTaskOutputType;
import com.ericsson.apex.model.policymodel.concepts.AxStateTaskReference;
import com.ericsson.apex.model.policymodel.concepts.AxTask;
import com.ericsson.apex.model.policymodel.concepts.AxTaskLogic;
import com.ericsson.apex.model.policymodel.concepts.AxTaskSelectionLogic;
import com.ericsson.apex.model.policymodel.concepts.AxTasks;
import com.ericsson.apex.model.policymodel.handling.PolicyLogicReader;
import com.ericsson.apex.model.utilities.ResourceUtils;

/**
 * The Class VPNDomainModelFactory is a factory that provides policy models for the VPN domain generated using Java.
 */
public class VPNDomainModelFactory {
    /**
     * Gets the VPN policy model.
     *
     * @return the VPN policy model
     */
    // CHECKSTYLE:OFF: checkstyle:maximumMethodLength
   public AxPolicyModel getVPNPolicyModel() {
        // CHECKSTYLE:ON: checkstyle:maximumMethodLength
        final String vpnCustomerArraySchemaString = ResourceUtils.getResourceAsString("com/ericsson/apex/domains/vpn/avro/model/avsc/VPNCustomerArray.avsc");
        final String vpnLinkMapSchemaString = ResourceUtils.getResourceAsString("com/ericsson/apex/domains/vpn/avro/model/avsc/VPNLinkMap.avsc");
        final String vpnCustomerMapSchemaString = ResourceUtils.getResourceAsString("com/ericsson/apex/domains/vpn/avro/model/avsc/VPNCustomerMap.avsc");

        // Data types for event parameters
        final AxContextSchema link = new AxContextSchema(new AxArtifactKey("Link", "0.0.1"), "Java", "java.lang.String");
        final AxContextSchema status = new AxContextSchema(new AxArtifactKey("Status", "0.0.1"), "Java", "java.lang.String");
        final AxContextSchema statusChanged = new AxContextSchema(new AxArtifactKey("StatusChanged", "0.0.1"), "Java", "java.lang.Boolean");
        final AxContextSchema affectedCustomers = new AxContextSchema(new AxArtifactKey("AffectedCustomers", "0.0.1"), "Avro", vpnCustomerArraySchemaString);
        final AxContextSchema problemStatus = new AxContextSchema(new AxArtifactKey("ProblemStatus", "0.0.1"), "Java", "java.lang.String");
        final AxContextSchema action = new AxContextSchema(new AxArtifactKey("Action", "0.0.1"), "Java", "java.lang.String");
        final AxContextSchema linkUp = new AxContextSchema(new AxArtifactKey("LinkUp", "0.0.1"), "Java", "java.lang.Boolean");
        final AxContextSchema customerName = new AxContextSchema(new AxArtifactKey("CustomerName", "0.0.1"), "Java", "java.lang.String");
        final AxContextSchema linkList = new AxContextSchema(new AxArtifactKey("LinkList", "0.0.1"), "Java", "java.lang.String");
        final AxContextSchema slaDT = new AxContextSchema(new AxArtifactKey("SlaDT", "0.0.1"), "Java", "java.lang.Integer");
        final AxContextSchema ytdDT = new AxContextSchema(new AxArtifactKey("YtdDT", "0.0.1"), "Java", "java.lang.Integer");
        final AxContextSchema linkMap = new AxContextSchema(new AxArtifactKey("LinkMap", "0.0.1"), "Avro", vpnLinkMapSchemaString);
        final AxContextSchema customerMap = new AxContextSchema(new AxArtifactKey("CustomerMap", "0.0.1"), "Avro", vpnCustomerMapSchemaString);

        final AxContextSchemas schemas = new AxContextSchemas(new AxArtifactKey("VPNDatatypes", "0.0.1"));
        schemas.getSchemasMap().put(link.getKey(), link);
        schemas.getSchemasMap().put(status.getKey(), status);
        schemas.getSchemasMap().put(statusChanged.getKey(), statusChanged);
        schemas.getSchemasMap().put(affectedCustomers.getKey(), affectedCustomers);
        schemas.getSchemasMap().put(problemStatus.getKey(), problemStatus);
        schemas.getSchemasMap().put(action.getKey(), action);
        schemas.getSchemasMap().put(linkUp.getKey(), linkUp);
        schemas.getSchemasMap().put(customerName.getKey(), customerName);
        schemas.getSchemasMap().put(linkList.getKey(), linkList);
        schemas.getSchemasMap().put(slaDT.getKey(), slaDT);
        schemas.getSchemasMap().put(ytdDT.getKey(), ytdDT);
        schemas.getSchemasMap().put(linkMap.getKey(), linkMap);
        schemas.getSchemasMap().put(customerMap.getKey(), customerMap);

        final AxEvent vpnLinkCtxtTriggerEvent = new AxEvent(new AxArtifactKey("VPNLinkCtxtTriggerEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnLinkCtxtTriggerEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnLinkCtxtTriggerEvent.getKey(), "Link"), link.getKey()));
        vpnLinkCtxtTriggerEvent.getParameterMap().put("LinkUp", new AxField(new AxReferenceKey(vpnLinkCtxtTriggerEvent.getKey(), "LinkUp"), linkUp.getKey()));

        final AxEvent vpnLinkCtxtMatchEvent = new AxEvent(new AxArtifactKey("VPNLinkCtxtMatchEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnLinkCtxtMatchEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnLinkCtxtMatchEvent.getKey(), "Link"), link.getKey()));
        vpnLinkCtxtMatchEvent.getParameterMap().put("LinkUp", new AxField(new AxReferenceKey(vpnLinkCtxtMatchEvent.getKey(), "LinkUp"), linkUp.getKey()));

        final AxEvent vpnLinkCtxtEstablishEvent = new AxEvent(new AxArtifactKey("VPNLinkCtxtEstablishEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnLinkCtxtEstablishEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnLinkCtxtEstablishEvent.getKey(), "Link"), link.getKey()));
        vpnLinkCtxtEstablishEvent.getParameterMap().put("LinkUp",
                new AxField(new AxReferenceKey(vpnLinkCtxtEstablishEvent.getKey(), "LinkUp"), linkUp.getKey()));

        final AxEvent vpnLinkCtxtDecideEvent = new AxEvent(new AxArtifactKey("VPNLinkCtxtDecideEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnLinkCtxtDecideEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnLinkCtxtDecideEvent.getKey(), "Link"), link.getKey()));
        vpnLinkCtxtDecideEvent.getParameterMap().put("LinkUp", new AxField(new AxReferenceKey(vpnLinkCtxtDecideEvent.getKey(), "LinkUp"), linkUp.getKey()));

        final AxEvent vpnLinkCtxtActEvent = new AxEvent(new AxArtifactKey("VPNLinkCtxtActEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnLinkCtxtActEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnLinkCtxtActEvent.getKey(), "Link"), link.getKey()));
        vpnLinkCtxtActEvent.getParameterMap().put("LinkUp", new AxField(new AxReferenceKey(vpnLinkCtxtActEvent.getKey(), "LinkUp"), linkUp.getKey()));

        final AxEvent vpnCustomerCtxtTriggerEvent = new AxEvent(new AxArtifactKey("VPNCustomerCtxtTriggerEvent", "0.0.1"),
                "com.ericsson.apex.domains.vpn.events");
        vpnCustomerCtxtTriggerEvent.getParameterMap().put("CustomerName",
                new AxField(new AxReferenceKey(vpnCustomerCtxtTriggerEvent.getKey(), "CustomerName"), customerName.getKey()));
        vpnCustomerCtxtTriggerEvent.getParameterMap().put("LinkList",
                new AxField(new AxReferenceKey(vpnCustomerCtxtTriggerEvent.getKey(), "LinkList"), linkList.getKey()));
        vpnCustomerCtxtTriggerEvent.getParameterMap().put("SlaDT",
                new AxField(new AxReferenceKey(vpnCustomerCtxtTriggerEvent.getKey(), "SlaDT"), slaDT.getKey()));
        vpnCustomerCtxtTriggerEvent.getParameterMap().put("YtdDT",
                new AxField(new AxReferenceKey(vpnCustomerCtxtTriggerEvent.getKey(), "YtdDT"), ytdDT.getKey()));

        final AxEvent vpnCustomerCtxtMatchEvent = new AxEvent(new AxArtifactKey("VPNCustomerCtxtMatchEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnCustomerCtxtMatchEvent.getParameterMap().put("CustomerName",
                new AxField(new AxReferenceKey(vpnCustomerCtxtMatchEvent.getKey(), "CustomerName"), customerName.getKey()));
        vpnCustomerCtxtMatchEvent.getParameterMap().put("LinkList",
                new AxField(new AxReferenceKey(vpnCustomerCtxtMatchEvent.getKey(), "LinkList"), linkList.getKey()));
        vpnCustomerCtxtMatchEvent.getParameterMap().put("SlaDT", new AxField(new AxReferenceKey(vpnCustomerCtxtMatchEvent.getKey(), "SlaDT"), slaDT.getKey()));
        vpnCustomerCtxtMatchEvent.getParameterMap().put("YtdDT", new AxField(new AxReferenceKey(vpnCustomerCtxtMatchEvent.getKey(), "YtdDT"), ytdDT.getKey()));

        final AxEvent vpnCustomerCtxtEstablishEvent = new AxEvent(new AxArtifactKey("VPNCustomerCtxtEstablishEvent", "0.0.1"),
                "com.ericsson.apex.domains.vpn.events");
        vpnCustomerCtxtEstablishEvent.getParameterMap().put("CustomerName",
                new AxField(new AxReferenceKey(vpnCustomerCtxtEstablishEvent.getKey(), "CustomerName"), customerName.getKey()));
        vpnCustomerCtxtEstablishEvent.getParameterMap().put("LinkList",
                new AxField(new AxReferenceKey(vpnCustomerCtxtEstablishEvent.getKey(), "LinkList"), linkList.getKey()));
        vpnCustomerCtxtEstablishEvent.getParameterMap().put("SlaDT",
                new AxField(new AxReferenceKey(vpnCustomerCtxtEstablishEvent.getKey(), "SlaDT"), slaDT.getKey()));
        vpnCustomerCtxtEstablishEvent.getParameterMap().put("YtdDT",
                new AxField(new AxReferenceKey(vpnCustomerCtxtEstablishEvent.getKey(), "YtdDT"), ytdDT.getKey()));

        final AxEvent vpnCustomerCtxtDecideEvent = new AxEvent(new AxArtifactKey("VPNCustomerCtxtDecideEvent", "0.0.1"),
                "com.ericsson.apex.domains.vpn.events");
        vpnCustomerCtxtDecideEvent.getParameterMap().put("CustomerName",
                new AxField(new AxReferenceKey(vpnCustomerCtxtDecideEvent.getKey(), "CustomerName"), customerName.getKey()));
        vpnCustomerCtxtDecideEvent.getParameterMap().put("LinkList",
                new AxField(new AxReferenceKey(vpnCustomerCtxtDecideEvent.getKey(), "LinkList"), linkList.getKey()));
        vpnCustomerCtxtDecideEvent.getParameterMap().put("SlaDT",
                new AxField(new AxReferenceKey(vpnCustomerCtxtDecideEvent.getKey(), "SlaDT"), slaDT.getKey()));
        vpnCustomerCtxtDecideEvent.getParameterMap().put("YtdDT",
                new AxField(new AxReferenceKey(vpnCustomerCtxtDecideEvent.getKey(), "YtdDT"), ytdDT.getKey()));

        final AxEvent vpnCustomerCtxtActEvent = new AxEvent(new AxArtifactKey("VPNCustomerCtxtActEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnCustomerCtxtActEvent.getParameterMap().put("CustomerName",
                new AxField(new AxReferenceKey(vpnCustomerCtxtActEvent.getKey(), "CustomerName"), customerName.getKey()));
        vpnCustomerCtxtActEvent.getParameterMap().put("LinkList",
                new AxField(new AxReferenceKey(vpnCustomerCtxtActEvent.getKey(), "LinkList"), linkList.getKey()));
        vpnCustomerCtxtActEvent.getParameterMap().put("SlaDT", new AxField(new AxReferenceKey(vpnCustomerCtxtActEvent.getKey(), "SlaDT"), slaDT.getKey()));
        vpnCustomerCtxtActEvent.getParameterMap().put("YtdDT", new AxField(new AxReferenceKey(vpnCustomerCtxtActEvent.getKey(), "YtdDT"), ytdDT.getKey()));

        final AxEvent vpnTriggerEvent = new AxEvent(new AxArtifactKey("VPNTriggerEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnTriggerEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnTriggerEvent.getKey(), "Link"), link.getKey()));
        vpnTriggerEvent.getParameterMap().put("Status", new AxField(new AxReferenceKey(vpnTriggerEvent.getKey(), "Status"), status.getKey()));

        final AxEvent vpnMatchEvent = new AxEvent(new AxArtifactKey("VPNMatchEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnMatchEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnMatchEvent.getKey(), "Link"), link.getKey()));
        vpnMatchEvent.getParameterMap().put("Status", new AxField(new AxReferenceKey(vpnMatchEvent.getKey(), "Status"), status.getKey()));
        vpnMatchEvent.getParameterMap().put("StatusChanged", new AxField(new AxReferenceKey(vpnMatchEvent.getKey(), "StatusChanged"), statusChanged.getKey()));

        final AxEvent vpnEstablishEvent = new AxEvent(new AxArtifactKey("VPNEstablishEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnEstablishEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnEstablishEvent.getKey(), "Link"), link.getKey()));
        vpnEstablishEvent.getParameterMap().put("AffectedCustomers",
                new AxField(new AxReferenceKey(vpnEstablishEvent.getKey(), "AffectedCustomers"), affectedCustomers.getKey()));
        vpnEstablishEvent.getParameterMap().put("ProblemStatus",
                new AxField(new AxReferenceKey(vpnEstablishEvent.getKey(), "ProblemStatus"), problemStatus.getKey()));

        final AxEvent vpnDecideEvent = new AxEvent(new AxArtifactKey("VPNDecideEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnDecideEvent.getParameterMap().put("Link", new AxField(new AxReferenceKey(vpnDecideEvent.getKey(), "Link"), link.getKey()));
        vpnDecideEvent.getParameterMap().put("Action", new AxField(new AxReferenceKey(vpnDecideEvent.getKey(), "Action"), action.getKey()));
        vpnDecideEvent.getParameterMap().put("AffectedCustomers",
                new AxField(new AxReferenceKey(vpnDecideEvent.getKey(), "AffectedCustomers"), affectedCustomers.getKey()));
        vpnDecideEvent.getParameterMap().put("ProblemStatus",
                new AxField(new AxReferenceKey(vpnDecideEvent.getKey(), "ProblemStatus"), problemStatus.getKey()));

        final AxEvent vpnActEvent = new AxEvent(new AxArtifactKey("VPNActEvent", "0.0.1"), "com.ericsson.apex.domains.vpn.events");
        vpnActEvent.getParameterMap().put("LinkMap", new AxField(new AxReferenceKey(vpnActEvent.getKey(), "LinkMap"), linkMap.getKey()));
        vpnActEvent.getParameterMap().put("CustomerMap", new AxField(new AxReferenceKey(vpnActEvent.getKey(), "CustomerMap"), customerMap.getKey()));

        final AxEvents vpnEvents = new AxEvents(new AxArtifactKey("VPNEvents", "0.0.1"));
        vpnEvents.getEventMap().put(vpnLinkCtxtTriggerEvent.getKey(), vpnLinkCtxtTriggerEvent);
        vpnEvents.getEventMap().put(vpnLinkCtxtMatchEvent.getKey(), vpnLinkCtxtMatchEvent);
        vpnEvents.getEventMap().put(vpnLinkCtxtEstablishEvent.getKey(), vpnLinkCtxtEstablishEvent);
        vpnEvents.getEventMap().put(vpnLinkCtxtDecideEvent.getKey(), vpnLinkCtxtDecideEvent);
        vpnEvents.getEventMap().put(vpnLinkCtxtActEvent.getKey(), vpnLinkCtxtActEvent);
        vpnEvents.getEventMap().put(vpnCustomerCtxtTriggerEvent.getKey(), vpnCustomerCtxtTriggerEvent);
        vpnEvents.getEventMap().put(vpnCustomerCtxtMatchEvent.getKey(), vpnCustomerCtxtMatchEvent);
        vpnEvents.getEventMap().put(vpnCustomerCtxtEstablishEvent.getKey(), vpnCustomerCtxtEstablishEvent);
        vpnEvents.getEventMap().put(vpnCustomerCtxtDecideEvent.getKey(), vpnCustomerCtxtDecideEvent);
        vpnEvents.getEventMap().put(vpnCustomerCtxtActEvent.getKey(), vpnCustomerCtxtActEvent);
        vpnEvents.getEventMap().put(vpnTriggerEvent.getKey(), vpnTriggerEvent);
        vpnEvents.getEventMap().put(vpnMatchEvent.getKey(), vpnMatchEvent);
        vpnEvents.getEventMap().put(vpnEstablishEvent.getKey(), vpnEstablishEvent);
        vpnEvents.getEventMap().put(vpnDecideEvent.getKey(), vpnDecideEvent);
        vpnEvents.getEventMap().put(vpnActEvent.getKey(), vpnActEvent);

        for (final AxEvent event : vpnEvents.getEventMap().values()) {
            event.setSource("Source");
            event.setTarget("Target");
        }

        final String vpnCustomerSchemaString = ResourceUtils.getResourceAsString("com/ericsson/apex/domains/vpn/avro/model/avsc/VPNCustomer.avsc");
        final String vpnLinkSchemaString = ResourceUtils.getResourceAsString("com/ericsson/apex/domains/vpn/avro/model/avsc/VPNLink.avsc");
        final String vpnProblemSchemaString = ResourceUtils.getResourceAsString("com/ericsson/apex/domains/vpn/avro/model/avsc/VPNProblem.avsc");

        // Data types for context
        final AxContextSchema vpnCustomer = new AxContextSchema(new AxArtifactKey("VPNCustomer", "0.0.1"), "Avro", vpnCustomerSchemaString);
        final AxContextSchema vpnLink = new AxContextSchema(new AxArtifactKey("VPNLink", "0.0.1"), "Avro", vpnLinkSchemaString);
        final AxContextSchema vpnProblem = new AxContextSchema(new AxArtifactKey("VPNProblem", "0.0.1"), "Avro", vpnProblemSchemaString);

        schemas.getSchemasMap().put(vpnCustomer.getKey(), vpnCustomer);
        schemas.getSchemasMap().put(vpnLink.getKey(), vpnLink);
        schemas.getSchemasMap().put(vpnProblem.getKey(), vpnProblem);

        // Three context maps for VPNs
        final AxContextAlbum vpnCustomerAlbum = new AxContextAlbum(new AxArtifactKey("VPNCustomerAlbum", "0.0.1"), "GLOBAL", true, vpnCustomer.getKey());
        final AxContextAlbum vpnLinkAlbum = new AxContextAlbum(new AxArtifactKey("VPNLinkAlbum", "0.0.1"), "GLOBAL", true, vpnLink.getKey());
        final AxContextAlbum vpnProblemAlbum = new AxContextAlbum(new AxArtifactKey("VPNProblemAlbum", "0.0.1"), "GLOBAL", true, vpnProblem.getKey());

        final AxContextAlbums vpnAlbums = new AxContextAlbums(new AxArtifactKey("VPNContext", "0.0.1"));
        vpnAlbums.getAlbumsMap().put(vpnCustomerAlbum.getKey(), vpnCustomerAlbum);
        vpnAlbums.getAlbumsMap().put(vpnLinkAlbum.getKey(), vpnLinkAlbum);
        vpnAlbums.getAlbumsMap().put(vpnProblemAlbum.getKey(), vpnProblemAlbum);

        final Set<AxArtifactKey> vpnContextReferenceSet = new TreeSet<>();
        vpnContextReferenceSet.add(vpnCustomerAlbum.getKey());
        vpnContextReferenceSet.add(vpnLinkAlbum.getKey());
        vpnContextReferenceSet.add(vpnProblemAlbum.getKey());

        // Tasks
        final AxLogicReader logicReader = new PolicyLogicReader().setLogicPackage(this.getClass().getPackage().getName()).setDefaultLogic("DefaultTask_Logic");

        final AxTask vpnLinkCtxtMatchTask = new AxTask(new AxArtifactKey("VPNLinkCtxtMatchTask", "0.0.1"));
        vpnLinkCtxtMatchTask.duplicateInputFields(vpnLinkCtxtTriggerEvent.getParameterMap());
        vpnLinkCtxtMatchTask.duplicateOutputFields(vpnLinkCtxtMatchEvent.getParameterMap());
        vpnLinkCtxtMatchTask.setTaskLogic(new AxTaskLogic(vpnLinkCtxtMatchTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnLinkCtxtEstablishTask = new AxTask(new AxArtifactKey("VPNLinkCtxtEstablishTask", "0.0.1"));
        vpnLinkCtxtEstablishTask.duplicateInputFields(vpnLinkCtxtMatchEvent.getParameterMap());
        vpnLinkCtxtEstablishTask.duplicateOutputFields(vpnLinkCtxtEstablishEvent.getParameterMap());
        vpnLinkCtxtEstablishTask.setTaskLogic(new AxTaskLogic(vpnLinkCtxtEstablishTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnLinkCtxtDecideTask = new AxTask(new AxArtifactKey("VPNLinkCtxtDecideTask", "0.0.1"));
        vpnLinkCtxtDecideTask.duplicateInputFields(vpnLinkCtxtEstablishEvent.getParameterMap());
        vpnLinkCtxtDecideTask.duplicateOutputFields(vpnLinkCtxtDecideEvent.getParameterMap());
        vpnLinkCtxtDecideTask.setTaskLogic(new AxTaskLogic(vpnLinkCtxtDecideTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnLinkCtxtActTask = new AxTask(new AxArtifactKey("VPNLinkCtxtActTask", "0.0.1"));
        vpnLinkCtxtActTask.duplicateInputFields(vpnLinkCtxtDecideEvent.getParameterMap());
        vpnLinkCtxtActTask.duplicateOutputFields(vpnLinkCtxtActEvent.getParameterMap());
        vpnLinkCtxtActTask.getContextAlbumReferences().add(vpnLinkAlbum.getKey());
        logicReader.setDefaultLogic(null);
        vpnLinkCtxtActTask.setTaskLogic(new AxTaskLogic(vpnLinkCtxtActTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnCustomerCtxtMatchTask = new AxTask(new AxArtifactKey("VPNCustomerCtxtMatchTask", "0.0.1"));
        vpnCustomerCtxtMatchTask.duplicateInputFields(vpnCustomerCtxtTriggerEvent.getParameterMap());
        vpnCustomerCtxtMatchTask.duplicateOutputFields(vpnCustomerCtxtMatchEvent.getParameterMap());
        logicReader.setDefaultLogic("DefaultTask_Logic");
        vpnCustomerCtxtMatchTask.setTaskLogic(new AxTaskLogic(vpnCustomerCtxtMatchTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnCustomerCtxtEstablishTask = new AxTask(new AxArtifactKey("VPNCustomerCtxtEstablishTask", "0.0.1"));
        vpnCustomerCtxtEstablishTask.duplicateInputFields(vpnCustomerCtxtMatchEvent.getParameterMap());
        vpnCustomerCtxtEstablishTask.duplicateOutputFields(vpnCustomerCtxtEstablishEvent.getParameterMap());
        vpnCustomerCtxtEstablishTask.setTaskLogic(new AxTaskLogic(vpnCustomerCtxtEstablishTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnCustomerCtxtDecideTask = new AxTask(new AxArtifactKey("VPNCustomerCtxtDecideTask", "0.0.1"));
        vpnCustomerCtxtDecideTask.duplicateInputFields(vpnCustomerCtxtEstablishEvent.getParameterMap());
        vpnCustomerCtxtDecideTask.duplicateOutputFields(vpnCustomerCtxtDecideEvent.getParameterMap());
        vpnCustomerCtxtDecideTask.setTaskLogic(new AxTaskLogic(vpnCustomerCtxtDecideTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnCustomerCtxtActTask = new AxTask(new AxArtifactKey("VPNCustomerCtxtActTask", "0.0.1"));
        vpnCustomerCtxtActTask.duplicateInputFields(vpnCustomerCtxtDecideEvent.getParameterMap());
        vpnCustomerCtxtActTask.duplicateOutputFields(vpnCustomerCtxtActEvent.getParameterMap());
        vpnCustomerCtxtActTask.getContextAlbumReferences().add(vpnCustomerAlbum.getKey());
        vpnCustomerCtxtActTask.getContextAlbumReferences().add(vpnLinkAlbum.getKey());
        logicReader.setDefaultLogic(null);
        vpnCustomerCtxtActTask.setTaskLogic(new AxTaskLogic(vpnCustomerCtxtActTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnMatchTask = new AxTask(new AxArtifactKey("VPNMatchTask", "0.0.1"));
        vpnMatchTask.duplicateInputFields(vpnTriggerEvent.getParameterMap());
        vpnMatchTask.duplicateOutputFields(vpnMatchEvent.getParameterMap());
        vpnMatchTask.getContextAlbumReferences().addAll(vpnContextReferenceSet);
        vpnMatchTask.setTaskLogic(new AxTaskLogic(vpnMatchTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnEstablishTask = new AxTask(new AxArtifactKey("VPNEstablishTask", "0.0.1"));
        vpnEstablishTask.duplicateInputFields(vpnMatchEvent.getParameterMap());
        vpnEstablishTask.duplicateOutputFields(vpnEstablishEvent.getParameterMap());
        vpnEstablishTask.getContextAlbumReferences().addAll(vpnContextReferenceSet);
        vpnEstablishTask.setTaskLogic(new AxTaskLogic(vpnEstablishTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnDecideSLATask = new AxTask(new AxArtifactKey("VPNDecideSLATask", "0.0.1"));
        vpnDecideSLATask.duplicateInputFields(vpnEstablishEvent.getParameterMap());
        vpnDecideSLATask.duplicateOutputFields(vpnDecideEvent.getParameterMap());
        vpnDecideSLATask.getContextAlbumReferences().addAll(vpnContextReferenceSet);
        vpnDecideSLATask.setTaskLogic(new AxTaskLogic(vpnDecideSLATask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnDecidePriorityTask = new AxTask(new AxArtifactKey("VPNDecidePriorityTask", "0.0.1"));
        vpnDecidePriorityTask.duplicateInputFields(vpnEstablishEvent.getParameterMap());
        vpnDecidePriorityTask.duplicateOutputFields(vpnDecideEvent.getParameterMap());
        vpnDecidePriorityTask.getContextAlbumReferences().addAll(vpnContextReferenceSet);
        vpnDecidePriorityTask.setTaskLogic(new AxTaskLogic(vpnDecidePriorityTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnDecideSolvedTask = new AxTask(new AxArtifactKey("VPNDecideSolvedTask", "0.0.1"));
        vpnDecideSolvedTask.duplicateInputFields(vpnEstablishEvent.getParameterMap());
        vpnDecideSolvedTask.duplicateOutputFields(vpnDecideEvent.getParameterMap());
        vpnDecideSolvedTask.getContextAlbumReferences().addAll(vpnContextReferenceSet);
        vpnDecideSolvedTask.setTaskLogic(new AxTaskLogic(vpnDecideSolvedTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnDecideTask = new AxTask(new AxArtifactKey("VPNDecideTask", "0.0.1"));
        vpnDecideTask.duplicateInputFields(vpnEstablishEvent.getParameterMap());
        vpnDecideTask.duplicateOutputFields(vpnDecideEvent.getParameterMap());
        vpnDecideTask.getContextAlbumReferences().addAll(vpnContextReferenceSet);
        logicReader.setDefaultLogic("DefaultTask_Logic");
        vpnDecideTask.setTaskLogic(new AxTaskLogic(vpnDecideTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTask vpnActTask = new AxTask(new AxArtifactKey("VPNActTask", "0.0.1"));
        vpnActTask.duplicateInputFields(vpnDecideEvent.getParameterMap());
        vpnActTask.duplicateOutputFields(vpnActEvent.getParameterMap());
        vpnActTask.getContextAlbumReferences().addAll(vpnContextReferenceSet);
        logicReader.setDefaultLogic(null);
        vpnActTask.setTaskLogic(new AxTaskLogic(vpnActTask.getKey(), "TaskLogic", "MVEL", logicReader));

        final AxTasks vpnTasks = new AxTasks(new AxArtifactKey("VPNTasks", "0.0.1"));
        vpnTasks.getTaskMap().put(vpnMatchTask.getKey(), vpnMatchTask);
        vpnTasks.getTaskMap().put(vpnEstablishTask.getKey(), vpnEstablishTask);
        vpnTasks.getTaskMap().put(vpnDecideTask.getKey(), vpnDecideTask);
        vpnTasks.getTaskMap().put(vpnDecidePriorityTask.getKey(), vpnDecidePriorityTask);
        vpnTasks.getTaskMap().put(vpnDecideSLATask.getKey(), vpnDecideSLATask);
        vpnTasks.getTaskMap().put(vpnDecideSolvedTask.getKey(), vpnDecideSolvedTask);
        vpnTasks.getTaskMap().put(vpnActTask.getKey(), vpnActTask);
        vpnTasks.getTaskMap().put(vpnLinkCtxtMatchTask.getKey(), vpnLinkCtxtMatchTask);
        vpnTasks.getTaskMap().put(vpnLinkCtxtEstablishTask.getKey(), vpnLinkCtxtEstablishTask);
        vpnTasks.getTaskMap().put(vpnLinkCtxtDecideTask.getKey(), vpnLinkCtxtDecideTask);
        vpnTasks.getTaskMap().put(vpnLinkCtxtActTask.getKey(), vpnLinkCtxtActTask);
        vpnTasks.getTaskMap().put(vpnCustomerCtxtMatchTask.getKey(), vpnCustomerCtxtMatchTask);
        vpnTasks.getTaskMap().put(vpnCustomerCtxtEstablishTask.getKey(), vpnCustomerCtxtEstablishTask);
        vpnTasks.getTaskMap().put(vpnCustomerCtxtDecideTask.getKey(), vpnCustomerCtxtDecideTask);
        vpnTasks.getTaskMap().put(vpnCustomerCtxtActTask.getKey(), vpnCustomerCtxtActTask);

        // Policies
        final AxPolicy vpnPolicy = new AxPolicy(new AxArtifactKey("VPNPolicy", "0.0.1"));
        vpnPolicy.setTemplate("MEDA");

        logicReader.setDefaultLogic("DefaultState_Logic");

        final AxState vpnActState = new AxState(new AxReferenceKey(vpnPolicy.getKey(), "Act"));
        vpnActState.setTrigger(vpnDecideEvent.getKey());
        final AxStateOutput act2Out = new AxStateOutput(vpnActState.getKey(), AxReferenceKey.getNullKey(), vpnActEvent.getKey());
        vpnActState.getStateOutputs().put(act2Out.getKey().getLocalName(), act2Out);
        vpnActState.setTaskSelectionLogic(new AxTaskSelectionLogic(vpnActState.getKey(), "TaskSelectionLogic", "MVEL", logicReader));
        vpnActState.setDefaultTask(vpnActTask.getKey());
        vpnActState.getTaskReferences().put(vpnActTask.getKey(),
                new AxStateTaskReference(vpnActState.getKey(), vpnActTask.getKey(), AxStateTaskOutputType.DIRECT, act2Out.getKey()));

        final AxState vpnDecideState = new AxState(new AxReferenceKey(vpnPolicy.getKey(), "Decide"));
        vpnDecideState.setTrigger(vpnEstablishEvent.getKey());
        final AxStateOutput decide2Act = new AxStateOutput(vpnDecideState.getKey(), vpnActState.getKey(), vpnDecideEvent.getKey());
        vpnDecideState.getStateOutputs().put(decide2Act.getKey().getLocalName(), decide2Act);
        logicReader.setDefaultLogic(null);
        vpnDecideState.setTaskSelectionLogic(new AxTaskSelectionLogic(vpnDecideState.getKey(), "TaskSelectionLogic", "MVEL", logicReader));
        vpnDecideState.setDefaultTask(vpnDecideTask.getKey());
        vpnDecideState.getTaskReferences().put(vpnDecideTask.getKey(),
                new AxStateTaskReference(vpnDecideState.getKey(), vpnDecideTask.getKey(), AxStateTaskOutputType.DIRECT, decide2Act.getKey()));
        vpnDecideState.getTaskReferences().put(vpnDecidePriorityTask.getKey(),
                new AxStateTaskReference(vpnDecideState.getKey(), vpnDecidePriorityTask.getKey(), AxStateTaskOutputType.DIRECT, decide2Act.getKey()));
        vpnDecideState.getTaskReferences().put(vpnDecideSLATask.getKey(),
                new AxStateTaskReference(vpnDecideState.getKey(), vpnDecideSLATask.getKey(), AxStateTaskOutputType.DIRECT, decide2Act.getKey()));
        vpnDecideState.getTaskReferences().put(vpnDecideSolvedTask.getKey(),
                new AxStateTaskReference(vpnDecideState.getKey(), vpnDecideSolvedTask.getKey(), AxStateTaskOutputType.DIRECT, decide2Act.getKey()));

        final AxState vpnEstablishState = new AxState(new AxReferenceKey(vpnPolicy.getKey(), "Establish"));
        vpnEstablishState.setTrigger(vpnMatchEvent.getKey());
        final AxStateOutput establish2Decide = new AxStateOutput(vpnEstablishState.getKey(), vpnDecideState.getKey(), vpnEstablishEvent.getKey());
        vpnEstablishState.getStateOutputs().put(establish2Decide.getKey().getLocalName(), establish2Decide);
        logicReader.setDefaultLogic("DefaultState_Logic");
        vpnEstablishState.setTaskSelectionLogic(new AxTaskSelectionLogic(vpnEstablishState.getKey(), "TaskSelectionLogic", "MVEL", logicReader));
        vpnEstablishState.setDefaultTask(vpnEstablishTask.getKey());
        vpnEstablishState.getTaskReferences().put(vpnEstablishTask.getKey(),
                new AxStateTaskReference(vpnEstablishState.getKey(), vpnEstablishTask.getKey(), AxStateTaskOutputType.DIRECT, establish2Decide.getKey()));

        final AxState vpnMatchState = new AxState(new AxReferenceKey(vpnPolicy.getKey(), "Match"));
        vpnMatchState.setTrigger(vpnTriggerEvent.getKey());
        final AxStateOutput match2Establish = new AxStateOutput(vpnMatchState.getKey(), vpnEstablishState.getKey(), vpnMatchEvent.getKey());
        vpnMatchState.getStateOutputs().put(match2Establish.getKey().getLocalName(), match2Establish);
        vpnMatchState.setTaskSelectionLogic(new AxTaskSelectionLogic(vpnMatchState.getKey(), "TaskSelectionLogic", "MVEL", logicReader));
        vpnMatchState.setDefaultTask(vpnMatchTask.getKey());
        vpnMatchState.getTaskReferences().put(vpnMatchTask.getKey(),
                new AxStateTaskReference(vpnMatchState.getKey(), vpnMatchTask.getKey(), AxStateTaskOutputType.DIRECT, match2Establish.getKey()));

        vpnPolicy.setFirstState(vpnMatchState.getKey().getLocalName());
        vpnPolicy.getStateMap().put(vpnMatchState.getKey().getLocalName(), vpnMatchState);
        vpnPolicy.getStateMap().put(vpnEstablishState.getKey().getLocalName(), vpnEstablishState);
        vpnPolicy.getStateMap().put(vpnDecideState.getKey().getLocalName(), vpnDecideState);
        vpnPolicy.getStateMap().put(vpnActState.getKey().getLocalName(), vpnActState);

        final AxPolicy vpnLinkCtxtPolicy = new AxPolicy(new AxArtifactKey("VPNLinkCtxtPolicy", "0.0.1"));
        vpnLinkCtxtPolicy.setTemplate("MEDA");

        final AxState vpnLinkCtxtActState = new AxState(new AxReferenceKey(vpnLinkCtxtPolicy.getKey(), "Act"));
        vpnLinkCtxtActState.setTrigger(vpnLinkCtxtDecideEvent.getKey());
        final AxStateOutput lcAct2Out = new AxStateOutput(vpnLinkCtxtActState.getKey(), AxReferenceKey.getNullKey(), vpnLinkCtxtActEvent.getKey());
        vpnLinkCtxtActState.getStateOutputs().put(lcAct2Out.getKey().getLocalName(), lcAct2Out);
        vpnLinkCtxtActState.setDefaultTask(vpnLinkCtxtActTask.getKey());
        vpnLinkCtxtActState.getTaskReferences().put(vpnLinkCtxtActTask.getKey(),
                new AxStateTaskReference(vpnLinkCtxtActState.getKey(), vpnLinkCtxtActTask.getKey(), AxStateTaskOutputType.DIRECT, lcAct2Out.getKey()));

        final AxState vpnLinkCtxtDecideState = new AxState(new AxReferenceKey(vpnLinkCtxtPolicy.getKey(), "Decide"));
        vpnLinkCtxtDecideState.setTrigger(vpnLinkCtxtEstablishEvent.getKey());
        final AxStateOutput lcDec2Act = new AxStateOutput(vpnLinkCtxtDecideState.getKey(), vpnLinkCtxtActState.getKey(), vpnLinkCtxtDecideEvent.getKey());
        vpnLinkCtxtDecideState.getStateOutputs().put(lcDec2Act.getKey().getLocalName(), lcDec2Act);
        vpnLinkCtxtDecideState.setDefaultTask(vpnLinkCtxtDecideTask.getKey());
        vpnLinkCtxtDecideState.getTaskReferences().put(vpnLinkCtxtDecideTask.getKey(),
                new AxStateTaskReference(vpnLinkCtxtDecideState.getKey(), vpnLinkCtxtDecideTask.getKey(), AxStateTaskOutputType.DIRECT, lcDec2Act.getKey()));

        final AxState vpnLinkCtxtEstablishState = new AxState(new AxReferenceKey(vpnLinkCtxtPolicy.getKey(), "Establish"));
        vpnLinkCtxtEstablishState.setTrigger(vpnLinkCtxtMatchEvent.getKey());
        final AxStateOutput lcEst2Dec = new AxStateOutput(vpnLinkCtxtEstablishState.getKey(), vpnLinkCtxtDecideState.getKey(),
                vpnLinkCtxtEstablishEvent.getKey());
        vpnLinkCtxtEstablishState.getStateOutputs().put(lcEst2Dec.getKey().getLocalName(), lcEst2Dec);
        vpnLinkCtxtEstablishState.setDefaultTask(vpnLinkCtxtEstablishTask.getKey());
        vpnLinkCtxtEstablishState.getTaskReferences().put(vpnLinkCtxtEstablishTask.getKey(), new AxStateTaskReference(vpnLinkCtxtEstablishState.getKey(),
                vpnLinkCtxtEstablishTask.getKey(), AxStateTaskOutputType.DIRECT, lcEst2Dec.getKey()));

        final AxState vpnLinkCtxtMatchState = new AxState(new AxReferenceKey(vpnLinkCtxtPolicy.getKey(), "Match"));
        vpnLinkCtxtMatchState.setTrigger(vpnLinkCtxtTriggerEvent.getKey());
        final AxStateOutput lcMat2Est = new AxStateOutput(vpnLinkCtxtMatchState.getKey(), vpnLinkCtxtEstablishState.getKey(), vpnLinkCtxtMatchEvent.getKey());
        vpnLinkCtxtMatchState.getStateOutputs().put(lcMat2Est.getKey().getLocalName(), lcMat2Est);
        vpnLinkCtxtMatchState.setDefaultTask(vpnLinkCtxtMatchTask.getKey());
        vpnLinkCtxtMatchState.getTaskReferences().put(vpnLinkCtxtMatchTask.getKey(),
                new AxStateTaskReference(vpnLinkCtxtMatchState.getKey(), vpnLinkCtxtMatchTask.getKey(), AxStateTaskOutputType.DIRECT, lcMat2Est.getKey()));

        vpnLinkCtxtPolicy.setFirstState(vpnLinkCtxtMatchState.getKey().getLocalName());
        vpnLinkCtxtPolicy.getStateMap().put(vpnLinkCtxtMatchState.getKey().getLocalName(), vpnLinkCtxtMatchState);
        vpnLinkCtxtPolicy.getStateMap().put(vpnLinkCtxtEstablishState.getKey().getLocalName(), vpnLinkCtxtEstablishState);
        vpnLinkCtxtPolicy.getStateMap().put(vpnLinkCtxtDecideState.getKey().getLocalName(), vpnLinkCtxtDecideState);
        vpnLinkCtxtPolicy.getStateMap().put(vpnLinkCtxtActState.getKey().getLocalName(), vpnLinkCtxtActState);

        final AxPolicy vpnCustomerCtxtPolicy = new AxPolicy(new AxArtifactKey("VPNCustomerCtxtPolicy", "0.0.1"));
        vpnCustomerCtxtPolicy.setTemplate("MEDA");

        final AxState vpnCustomerCtxtActState = new AxState(new AxReferenceKey(vpnCustomerCtxtPolicy.getKey(), "Act"));
        vpnCustomerCtxtActState.setTrigger(vpnCustomerCtxtDecideEvent.getKey());
        final AxStateOutput ccAct2Out = new AxStateOutput(vpnCustomerCtxtActState.getKey(), AxReferenceKey.getNullKey(), vpnCustomerCtxtActEvent.getKey());
        vpnCustomerCtxtActState.getStateOutputs().put(ccAct2Out.getKey().getLocalName(), ccAct2Out);
        vpnCustomerCtxtActState.setDefaultTask(vpnCustomerCtxtActTask.getKey());
        vpnCustomerCtxtActState.getTaskReferences().put(vpnCustomerCtxtActTask.getKey(),
                new AxStateTaskReference(vpnCustomerCtxtActState.getKey(), vpnCustomerCtxtActTask.getKey(), AxStateTaskOutputType.DIRECT, ccAct2Out.getKey()));

        final AxState vpnCustomerCtxtDecideState = new AxState(new AxReferenceKey(vpnCustomerCtxtPolicy.getKey(), "Decide"));
        vpnCustomerCtxtDecideState.setTrigger(vpnCustomerCtxtEstablishEvent.getKey());
        final AxStateOutput ccDec2Act = new AxStateOutput(vpnCustomerCtxtDecideState.getKey(), vpnCustomerCtxtActState.getKey(),
                vpnCustomerCtxtDecideEvent.getKey());
        vpnCustomerCtxtDecideState.getStateOutputs().put(ccDec2Act.getKey().getLocalName(), ccDec2Act);
        vpnCustomerCtxtDecideState.setDefaultTask(vpnCustomerCtxtDecideTask.getKey());
        vpnCustomerCtxtDecideState.getTaskReferences().put(vpnCustomerCtxtDecideTask.getKey(), new AxStateTaskReference(vpnCustomerCtxtDecideState.getKey(),
                vpnCustomerCtxtDecideTask.getKey(), AxStateTaskOutputType.DIRECT, ccDec2Act.getKey()));

        final AxState vpnCustomerCtxtEstablishState = new AxState(new AxReferenceKey(vpnCustomerCtxtPolicy.getKey(), "Establish"));
        vpnCustomerCtxtEstablishState.setTrigger(vpnCustomerCtxtMatchEvent.getKey());
        final AxStateOutput ccEst2Dec = new AxStateOutput(vpnCustomerCtxtEstablishState.getKey(), vpnCustomerCtxtDecideState.getKey(),
                vpnCustomerCtxtEstablishEvent.getKey());
        vpnCustomerCtxtEstablishState.getStateOutputs().put(ccEst2Dec.getKey().getLocalName(), ccEst2Dec);
        vpnCustomerCtxtEstablishState.setDefaultTask(vpnCustomerCtxtEstablishTask.getKey());
        vpnCustomerCtxtEstablishState.getTaskReferences().put(vpnCustomerCtxtEstablishTask.getKey(), new AxStateTaskReference(
                vpnCustomerCtxtEstablishState.getKey(), vpnCustomerCtxtEstablishTask.getKey(), AxStateTaskOutputType.DIRECT, ccEst2Dec.getKey()));

        final AxState vpnCustomerCtxtMatchState = new AxState(new AxReferenceKey(vpnCustomerCtxtPolicy.getKey(), "Match"));
        vpnCustomerCtxtMatchState.setTrigger(vpnCustomerCtxtTriggerEvent.getKey());
        final AxStateOutput ccMat2Est = new AxStateOutput(vpnCustomerCtxtMatchState.getKey(), vpnCustomerCtxtEstablishState.getKey(),
                vpnCustomerCtxtMatchEvent.getKey());
        vpnCustomerCtxtMatchState.getStateOutputs().put(ccMat2Est.getKey().getLocalName(), ccMat2Est);
        vpnCustomerCtxtMatchState.setDefaultTask(vpnCustomerCtxtMatchTask.getKey());
        vpnCustomerCtxtMatchState.getTaskReferences().put(vpnCustomerCtxtMatchTask.getKey(), new AxStateTaskReference(vpnCustomerCtxtMatchState.getKey(),
                vpnCustomerCtxtMatchTask.getKey(), AxStateTaskOutputType.DIRECT, ccMat2Est.getKey()));

        vpnCustomerCtxtPolicy.setFirstState(vpnCustomerCtxtMatchState.getKey().getLocalName());
        vpnCustomerCtxtPolicy.getStateMap().put(vpnCustomerCtxtMatchState.getKey().getLocalName(), vpnCustomerCtxtMatchState);
        vpnCustomerCtxtPolicy.getStateMap().put(vpnCustomerCtxtEstablishState.getKey().getLocalName(), vpnCustomerCtxtEstablishState);
        vpnCustomerCtxtPolicy.getStateMap().put(vpnCustomerCtxtDecideState.getKey().getLocalName(), vpnCustomerCtxtDecideState);
        vpnCustomerCtxtPolicy.getStateMap().put(vpnCustomerCtxtActState.getKey().getLocalName(), vpnCustomerCtxtActState);

        final AxPolicies vpnPolicies = new AxPolicies(new AxArtifactKey("VPNPolicies", "0.0.1"));
        vpnPolicies.getPolicyMap().put(vpnPolicy.getKey(), vpnPolicy);
        vpnPolicies.getPolicyMap().put(vpnLinkCtxtPolicy.getKey(), vpnLinkCtxtPolicy);
        vpnPolicies.getPolicyMap().put(vpnCustomerCtxtPolicy.getKey(), vpnCustomerCtxtPolicy);

        final AxKeyInformation keyInformation = new AxKeyInformation(new AxArtifactKey("VPNKeyInformation", "0.0.1"));
        final AxPolicyModel vpnPolicyModel = new AxPolicyModel(new AxArtifactKey("VPNPolicyModelAvro", "0.0.1"));
        vpnPolicyModel.setPolicies(vpnPolicies);
        vpnPolicyModel.setEvents(vpnEvents);
        vpnPolicyModel.setTasks(vpnTasks);
        vpnPolicyModel.setAlbums(vpnAlbums);
        vpnPolicyModel.setSchemas(schemas);
        vpnPolicyModel.setKeyInformation(keyInformation);
        vpnPolicyModel.getKeyInformation().generateKeyInfo(vpnPolicyModel);

        final AxValidationResult result = vpnPolicyModel.validate(new AxValidationResult());
        if (!result.getValidationResult().equals(AxValidationResult.ValidationResult.VALID)) {
            throw new ApexRuntimeException("model " + vpnPolicyModel.getID() + " is not valid" + result);
        }
        return vpnPolicyModel;
    }
}

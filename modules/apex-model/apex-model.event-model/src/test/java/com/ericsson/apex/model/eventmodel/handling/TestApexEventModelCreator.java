/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.eventmodel.handling;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.test.TestApexModelCreator;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;
import com.ericsson.apex.model.eventmodel.concepts.AxEvent;
import com.ericsson.apex.model.eventmodel.concepts.AxEventModel;
import com.ericsson.apex.model.eventmodel.concepts.AxEvents;
import com.ericsson.apex.model.eventmodel.concepts.AxField;

public class TestApexEventModelCreator implements TestApexModelCreator<AxEventModel> {

    @Override
    public AxEventModel getModel() {
        AxContextSchema axSchema0 = new AxContextSchema(new AxArtifactKey("BooleanType", "0.0.1"), "Java", "java.lang.Boolean");
        AxContextSchema axSchema1 = new AxContextSchema(new AxArtifactKey("IntType",     "0.0.1"), "Java", "java.lang.Integer");
        AxContextSchema axSchema2 = new AxContextSchema(new AxArtifactKey("StringType",  "0.0.1"), "Java", "java.lang.String");
        AxContextSchema axSchema3 = new AxContextSchema(new AxArtifactKey("KeyType",     "0.0.1"), "Java", "com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey");
        AxContextSchema axSchema4 = new AxContextSchema(new AxArtifactKey("MapType",     "0.0.1"), "Java", "com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation");
        AxContextSchema axSchema5 = new AxContextSchema(new AxArtifactKey("BigIntType",  "0.0.1"), "Java", "java.math.BigInteger");
        AxContextSchema axSchema6 = new AxContextSchema(new AxArtifactKey("ModelType",   "0.0.1"), "Java", "com.ericsson.apex.model.basicmodel.concepts.AxModel");
        
        AxContextSchemas dataTypes = new AxContextSchemas(new AxArtifactKey("Schemas", "0.0.1"));
        dataTypes.getSchemasMap().put(axSchema0.getKey(), axSchema0);
        dataTypes.getSchemasMap().put(axSchema1.getKey(), axSchema1);
        dataTypes.getSchemasMap().put(axSchema2.getKey(), axSchema2);
        dataTypes.getSchemasMap().put(axSchema3.getKey(), axSchema3);
        dataTypes.getSchemasMap().put(axSchema4.getKey(), axSchema4);
        dataTypes.getSchemasMap().put(axSchema5.getKey(), axSchema5);
        dataTypes.getSchemasMap().put(axSchema6.getKey(), axSchema6);
        
        AxEvents eventMap = new AxEvents(new AxArtifactKey("smallEventMap", "0.0.1"));

        AxEvent event0 = new AxEvent(new AxArtifactKey("event0", "0.0.1"), "com.ericsson.apex.model.eventmodel.events", "Source", "Target");
        event0.getParameterMap().put("par0", new AxField(new AxReferenceKey(event0.getKey(), "par0"), axSchema0.getKey()));
        event0.getParameterMap().put("par1", new AxField(new AxReferenceKey(event0.getKey(), "par1"), axSchema1.getKey()));
        event0.getParameterMap().put("par2", new AxField(new AxReferenceKey(event0.getKey(), "par2"), axSchema2.getKey()));
        event0.getParameterMap().put("par3", new AxField(new AxReferenceKey(event0.getKey(), "par3"), axSchema6.getKey()));
        event0.getParameterMap().put("par4", new AxField(new AxReferenceKey(event0.getKey(), "par4"), axSchema4.getKey()));
        event0.getParameterMap().put("par5", new AxField(new AxReferenceKey(event0.getKey(), "par5"), axSchema5.getKey()));
        event0.getParameterMap().put("par6", new AxField(new AxReferenceKey(event0.getKey(), "par6"), axSchema5.getKey()));
        eventMap.getEventMap().put(event0.getKey(), event0);

        AxEvent event1 = new AxEvent(new AxArtifactKey("event1", "0.0.1"), "com.ericsson.apex.model.eventmodel.events", "Source", "Target");
        event1.getParameterMap().put("theOnlyPar", new AxField(new AxReferenceKey(event1.getKey(), "theOnlyPar"), axSchema3.getKey()));
        eventMap.getEventMap().put(event1.getKey(), event1);

        AxEvent event2 = new AxEvent(new AxArtifactKey("event2", "0.0.1"), "com.ericsson.apex.model.eventmodel.events", "Source", "Target");
        eventMap.getEventMap().put(event2.getKey(), event2);

        AxKeyInformation keyInformation = new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1"));

        AxEventModel eventModel = new AxEventModel(new AxArtifactKey("EventModel", "0.0.1"), dataTypes, keyInformation, eventMap);
        keyInformation.generateKeyInfo(eventModel);
        
        eventModel.validate(new AxValidationResult());
        return eventModel;
    }
    
    @Override
    public AxEventModel getInvalidModel() {
        AxContextSchema axSchema0 = new AxContextSchema(new AxArtifactKey("BooleanType", "0.0.1"), "Java", "java.lang.Zoolean");
        AxContextSchema axSchema1 = new AxContextSchema(new AxArtifactKey("IntType",     "0.0.1"), "Java", "java.lang.Integer");
        AxContextSchema axSchema2 = new AxContextSchema(new AxArtifactKey("StringType",  "0.0.1"), "Java", "java.lang.String");
        AxContextSchema axSchema3 = new AxContextSchema(new AxArtifactKey("SetType",     "0.0.1"), "Java", "java.util.Set");
        AxContextSchema axSchema4 = new AxContextSchema(new AxArtifactKey("MapType",     "0.0.1"), "Java", "java.util.Map");
        AxContextSchema axSchema5 = new AxContextSchema(new AxArtifactKey("BigIntType",  "0.0.1"), "Java", "java.math.BigInteger");
        
        AxContextSchemas dataTypes = new AxContextSchemas(new AxArtifactKey("Schemas", "0.0.1"));
        dataTypes.getSchemasMap().put(axSchema0.getKey(), axSchema0);
        dataTypes.getSchemasMap().put(axSchema1.getKey(), axSchema1);
        dataTypes.getSchemasMap().put(axSchema2.getKey(), axSchema2);
        dataTypes.getSchemasMap().put(axSchema3.getKey(), axSchema3);
        dataTypes.getSchemasMap().put(axSchema4.getKey(), axSchema4);
        dataTypes.getSchemasMap().put(axSchema5.getKey(), axSchema5);
        
        AxEvents eventMap = new AxEvents(new AxArtifactKey("smallEventMap", "0.0.1"));

        AxEvent event0 = new AxEvent(new AxArtifactKey("event0", "0.0.1"), "com.ericsson.apex.model.eventmodel.events");
        event0.getParameterMap().put("par0", new AxField(new AxReferenceKey(event0.getKey(), "par0"), axSchema0.getKey()));
        event0.getParameterMap().put("par1", new AxField(new AxReferenceKey(event0.getKey(), "par1"), axSchema1.getKey()));
        event0.getParameterMap().put("par2", new AxField(new AxReferenceKey(event0.getKey(), "par2"), axSchema2.getKey()));
        event0.getParameterMap().put("par3", new AxField(new AxReferenceKey(event0.getKey(), "par3"), axSchema3.getKey()));
        event0.getParameterMap().put("par4", new AxField(new AxReferenceKey(event0.getKey(), "par4"), axSchema4.getKey()));
        event0.getParameterMap().put("par5", new AxField(new AxReferenceKey(event0.getKey(), "par5"), axSchema5.getKey()));
        event0.getParameterMap().put("par6", new AxField(new AxReferenceKey(event0.getKey(), "par6"), axSchema5.getKey()));
        eventMap.getEventMap().put(event0.getKey(), event0);

        AxEvent event1 = new AxEvent(new AxArtifactKey("event1", "0.0.1"), "com.ericsson.apex.model.eventmodel.events");
        event1.getParameterMap().put("theOnlyPar", new AxField(new AxReferenceKey(event0.getKey(), "theOnlyPar"), axSchema3.getKey()));
        eventMap.getEventMap().put(event1.getKey(), event1);

        AxEvent event2 = new AxEvent(new AxArtifactKey("event2", "0.0.1"), "com.ericsson.apex.model.eventmodel.events");
        eventMap.getEventMap().put(event2.getKey(), event1);

        AxKeyInformation keyInformation = new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1"));

        AxEventModel eventModel = new AxEventModel(new AxArtifactKey("smallEventModel", "0.0.1"), dataTypes, keyInformation, eventMap);
        
        return eventModel;
    }

    public AxEventModel getMalstructuredModel() {
        AxContextSchema axSchema3 = new AxContextSchema(new AxArtifactKey("SetType", "0.0.1"), "Java", "java.util.Set");
        AxContextSchemas dataTypes = new AxContextSchemas(new AxArtifactKey("Schemas", "0.0.1"));
        dataTypes.getSchemasMap().put(axSchema3.getKey(), axSchema3);
        
        AxEvents eventMap = new AxEvents(new AxArtifactKey("smallEventMap", "0.0.1"));

        AxEvent event1 = new AxEvent(new AxArtifactKey("event1", "0.0.1"), "com.ericsson.apex.model.eventmodel.events");
        event1.getParameterMap().put("theOnlyPar", new AxField(new AxReferenceKey(event1.getKey(), "theOnlyPar"), axSchema3.getKey()));
        eventMap.getEventMap().put(event1.getKey(), event1);

        AxEvent event2 = new AxEvent(new AxArtifactKey("event2", "0.0.1"), "com.ericsson.apex.model.eventmodel.events");
        eventMap.getEventMap().put(event2.getKey(), event1);

        AxKeyInformation keyInformation = new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1"));

        AxEventModel eventModel = new AxEventModel(new AxArtifactKey("smallEventModel", "0.0.1"), dataTypes, keyInformation, eventMap);
        
        eventModel.validate(new AxValidationResult());
        
        return eventModel;
    }

    @Override
    public AxEventModel getWarningModel() {
        AxContextSchema axSchema0 = new AxContextSchema(new AxArtifactKey("BooleanType", "0.0.1"), "Java", "java.lang.Boolean");
        AxContextSchema axSchema1 = new AxContextSchema(new AxArtifactKey("IntType",     "0.0.1"), "Java", "java.lang.Integer");
        AxContextSchema axSchema2 = new AxContextSchema(new AxArtifactKey("StringType",  "0.0.1"), "Java", "java.lang.String");
        AxContextSchema axSchema3 = new AxContextSchema(new AxArtifactKey("SetType",     "0.0.1"), "Java", "java.util.Set");
        AxContextSchema axSchema4 = new AxContextSchema(new AxArtifactKey("MapType",     "0.0.1"), "Java", "java.util.Map");
        AxContextSchema axSchema5 = new AxContextSchema(new AxArtifactKey("BigIntType",  "0.0.1"), "Java", "java.math.BigInteger");
        AxContextSchemas dataTypes = new AxContextSchemas(new AxArtifactKey("Schemas", "0.0.1"));
        dataTypes.getSchemasMap().put(axSchema0.getKey(), axSchema0);
        dataTypes.getSchemasMap().put(axSchema1.getKey(), axSchema1);
        dataTypes.getSchemasMap().put(axSchema2.getKey(), axSchema2);
        dataTypes.getSchemasMap().put(axSchema3.getKey(), axSchema3);
        dataTypes.getSchemasMap().put(axSchema4.getKey(), axSchema4);
        dataTypes.getSchemasMap().put(axSchema5.getKey(), axSchema5);
        
        AxEvents eventMap = new AxEvents(new AxArtifactKey("smallEventMap", "0.0.1"));

        AxEvent event0 = new AxEvent(new AxArtifactKey("event0", "0.0.1"), "");
        event0.getParameterMap().put("par0", new AxField(new AxReferenceKey(event0.getKey(), "par0"), axSchema0.getKey()));
        event0.getParameterMap().put("par1", new AxField(new AxReferenceKey(event0.getKey(), "par1"), axSchema1.getKey()));
        event0.getParameterMap().put("par2", new AxField(new AxReferenceKey(event0.getKey(), "par2"), axSchema2.getKey()));
        event0.getParameterMap().put("par3", new AxField(new AxReferenceKey(event0.getKey(), "par3"), axSchema3.getKey()));
        event0.getParameterMap().put("par4", new AxField(new AxReferenceKey(event0.getKey(), "par4"), axSchema4.getKey()));
        event0.getParameterMap().put("par5", new AxField(new AxReferenceKey(event0.getKey(), "par5"), axSchema5.getKey()));
        eventMap.getEventMap().put(event0.getKey(), event0);

        AxEvent event1 = new AxEvent(new AxArtifactKey("event1", "0.0.1"), "");
        event1.getParameterMap().put("theOnlyPar", new AxField(new AxReferenceKey(event1.getKey(), "theOnlyPar"), axSchema3.getKey()));
        eventMap.getEventMap().put(event1.getKey(), event1);

        AxEvent event2 = new AxEvent(new AxArtifactKey("event2", "0.0.1"), "");
        eventMap.getEventMap().put(event2.getKey(), event2);

        AxKeyInformation keyInformation = new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1"));

        AxEventModel eventModel = new AxEventModel(new AxArtifactKey("smallEventModel", "0.0.1"), dataTypes, keyInformation, eventMap);
        eventModel.getKeyInformation().generateKeyInfo(eventModel);
        eventModel.validate(new AxValidationResult());
        
        return eventModel;
    }

    @Override
    public AxEventModel getObservationModel() {
        AxContextSchema axSchema0 = new AxContextSchema(new AxArtifactKey("BooleanType", "0.0.1"), "Java", "java.lang.Boolean");
        AxContextSchema axSchema1 = new AxContextSchema(new AxArtifactKey("IntType",     "0.0.1"), "Java", "java.lang.Integer");
        AxContextSchema axSchema2 = new AxContextSchema(new AxArtifactKey("StringType",  "0.0.1"), "Java", "java.lang.String");
        AxContextSchema axSchema3 = new AxContextSchema(new AxArtifactKey("SetType",     "0.0.1"), "Java", "java.util.Set");
        AxContextSchema axSchema4 = new AxContextSchema(new AxArtifactKey("MapType",     "0.0.1"), "Java", "java.util.Map");
        AxContextSchema axSchema5 = new AxContextSchema(new AxArtifactKey("BigIntType",  "0.0.1"), "Java", "java.math.BigInteger");
        AxContextSchemas schemas = new AxContextSchemas(new AxArtifactKey("Schemas", "0.0.1"));
        schemas.getSchemasMap().put(axSchema0.getKey(), axSchema0);
        schemas.getSchemasMap().put(axSchema1.getKey(), axSchema1);
        schemas.getSchemasMap().put(axSchema2.getKey(), axSchema2);
        schemas.getSchemasMap().put(axSchema3.getKey(), axSchema3);
        schemas.getSchemasMap().put(axSchema4.getKey(), axSchema4);
        schemas.getSchemasMap().put(axSchema5.getKey(), axSchema5);
        
        AxEvents eventMap = new AxEvents(new AxArtifactKey("smallEventMap", "0.0.1"));

        AxEvent event0 = new AxEvent(new AxArtifactKey("event0", "0.0.1"), "com.ericsson.apex.model.eventmodel.events");
        event0.getParameterMap().put("par0", new AxField(new AxReferenceKey(event0.getKey(), "par0"), axSchema0.getKey()));
        event0.getParameterMap().put("par1", new AxField(new AxReferenceKey(event0.getKey(), "par1"), axSchema1.getKey()));
        event0.getParameterMap().put("par2", new AxField(new AxReferenceKey(event0.getKey(), "par2"), axSchema2.getKey()));
        event0.getParameterMap().put("par3", new AxField(new AxReferenceKey(event0.getKey(), "par3"), axSchema3.getKey()));
        event0.getParameterMap().put("par4", new AxField(new AxReferenceKey(event0.getKey(), "par4"), axSchema4.getKey()));
        event0.getParameterMap().put("par5", new AxField(new AxReferenceKey(event0.getKey(), "par5"), axSchema5.getKey()));
        eventMap.getEventMap().put(event0.getKey(), event0);

        AxEvent event1 = new AxEvent(new AxArtifactKey("event1", "0.0.1"), "com.ericsson.apex.model.eventmodel.events");
        event1.getParameterMap().put("theOnlyPar", new AxField(new AxReferenceKey(event1.getKey(), "theOnlyPar"), axSchema3.getKey()));
        eventMap.getEventMap().put(event1.getKey(), event1);

        AxEvent event2 = new AxEvent(new AxArtifactKey("event2", "0.0.1"), "com.ericsson.apex.model.eventmodel.events");
        eventMap.getEventMap().put(event2.getKey(), event2);

        AxKeyInformation keyInformation = new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1"));

        AxEventModel eventModel = new AxEventModel(new AxArtifactKey("smallEventModel", "0.0.1"), schemas, keyInformation, eventMap);
        eventModel.getKeyInformation().generateKeyInfo(eventModel);
        eventModel.validate(new AxValidationResult());
        
        return eventModel;
    }
}

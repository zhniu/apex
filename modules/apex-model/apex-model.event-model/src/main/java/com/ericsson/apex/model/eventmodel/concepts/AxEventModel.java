/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.eventmodel.concepts;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * A container class for an Apex event model. This class is a container class that allows an Apex model to be constructed that contains events and context and
 * the key information for those events and context. The model contains schema definitions and the definitions of events that use those schemas.
 * <p>
 * Validation runs {@link AxModel} validation on the model. In addition, the {@link AxContextSchemas} and {@link AxEvents} validation is run on the context
 * schemas and events in the model.
 */

@Entity
@Table(name = "AxEventModel")

@XmlRootElement(name = "apexEventModel", namespace = "http://www.ericsson.com/apex")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AxEventModel", namespace = "http://www.ericsson.com/apex", propOrder = { "schemas", "events" })

public class AxEventModel extends AxModel {
    private static final long serialVersionUID = 8800599637708309945L;

    // @formatter:off
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "schemasName", referencedColumnName = "name"),
        @JoinColumn(name = "schemasVersion", referencedColumnName = "version")
    })
    @XmlElement(name = "schemas", required = true)
    private AxContextSchemas schemas;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "eventsName", referencedColumnName = "name"),
        @JoinColumn(name = "eventsVersion", referencedColumnName = "version")
    })
    @XmlElement(name = "events", required = true)
    private AxEvents events;
    // @formatter:on

    /**
     * The Default Constructor creates a {@link AxEventModel} object with a null artifact key and creates an empty event model.
     */
    public AxEventModel() {
        this(new AxArtifactKey());
    }

    /**
     * The Key Constructor creates a {@link AxEventModel} object with the given artifact key and creates an empty event model.
     *
     * @param key the event model key
     */
    public AxEventModel(final AxArtifactKey key) {
        this(key, new AxContextSchemas(new AxArtifactKey(key.getName() + "_Schemas", key.getVersion())),
                new AxKeyInformation(new AxArtifactKey(key.getName() + "_KeyInfo", key.getVersion())),
                new AxEvents(new AxArtifactKey(key.getName() + "_Events", key.getVersion())));
    }

    /**
     * Constructor that initiates a {@link AxEventModel} with all its fields.
     *
     * @param key the event model key
     * @param schemas the schemas for events in the event model
     * @param keyInformation the key information for context schemas and events in the event model
     * @param events the events in the event model
     */
    public AxEventModel(final AxArtifactKey key, final AxContextSchemas schemas, final AxKeyInformation keyInformation, final AxEvents events) {
        super(key, keyInformation);
        Assertions.argumentNotNull(events, "events may not be null");

        this.schemas = schemas;
        this.events = events;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#register()
     */
    @Override
    public void register() {
        super.register();
        ModelService.registerModel(AxContextSchemas.class, getSchemas());
        ModelService.registerModel(AxEvents.class, getEvents());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#getKeys()
     */
    @Override
    public List<AxKey> getKeys() {
        final List<AxKey> keyList = super.getKeys();

        keyList.addAll(schemas.getKeys());
        keyList.addAll(events.getKeys());

        return keyList;
    }

    /**
     * Gets the context schemas.
     *
     * @return the context schemas
     */
    public AxContextSchemas getSchemas() {
        return schemas;
    }

    /**
     * Sets the context schemas.
     *
     * @param schemas the context schemas
     */
    public void setSchemas(final AxContextSchemas schemas) {
        Assertions.argumentNotNull(schemas, "schemas may not be null");
        this.schemas = schemas;
    }

    /**
     * Gets the events from the model.
     *
     * @return the events
     */
    public AxEvents getEvents() {
        return events;
    }

    /**
     * Sets the events in the model.
     *
     * @param events the events
     */
    public void setEvents(final AxEvents events) {
        Assertions.argumentNotNull(events, "events may not be null");
        this.events = events;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#validate(com.ericsson.apex.model.basicmodel.concepts.AxValidationResult)
     */
    @Override
    public AxValidationResult validate(final AxValidationResult resultIn) {
        AxValidationResult result = resultIn;

        result = super.validate(result);
        result = schemas.validate(result);
        return events.validate(result);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#clean()
     */
    @Override
    public void clean() {
        super.clean();
        schemas.clean();
        events.clean();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(":(");
        builder.append(super.toString());
        builder.append(",schemas=");
        builder.append(schemas);
        builder.append(",events=");
        builder.append(events);
        builder.append(")");
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#clone()
     */
    @Override
    public Object clone() {
        return copyTo(new AxEventModel());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#copyTo(java.lang.Object)
     */
    @Override
    public Object copyTo(final Object target) {
        Assertions.argumentNotNull(target, "target may not be null");

        final Object copyObject = target;
        Assertions.instanceOf(copyObject, AxEventModel.class);

        final AxEventModel copy = ((AxEventModel) copyObject);
        super.copyTo(target);
        copy.setSchemas((AxContextSchemas) schemas.clone());
        copy.setEvents((AxEvents) events.clone());

        return copyObject;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + super.hashCode();
        result = prime * result + schemas.hashCode();
        result = prime * result + events.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("comparison object may not be null");
        }

        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final AxEventModel other = (AxEventModel) obj;
        if (!super.equals(other)) {
            return false;
        }
        if (!schemas.equals(other.schemas)) {
            return false;
        }
        return events.equals(other.events);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxModel#compareTo(com.ericsson.apex.model.basicmodel.concepts.AxConcept)
     */
    @Override
    public int compareTo(final AxConcept otherObj) {
        Assertions.argumentNotNull(otherObj, "comparison object may not be null");

        if (this == otherObj) {
            return 0;
        }
        if (getClass() != otherObj.getClass()) {
            return this.hashCode() - otherObj.hashCode();
        }

        final AxEventModel other = (AxEventModel) otherObj;
        if (!super.equals(other)) {
            return super.compareTo(other);
        }
        if (!schemas.equals(other.schemas)) {
            return schemas.compareTo(other.schemas);
        }
        return events.compareTo(other.events);
    }
}

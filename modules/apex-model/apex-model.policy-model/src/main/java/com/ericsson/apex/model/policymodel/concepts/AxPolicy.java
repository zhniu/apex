/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.concepts;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationMessage;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class holds the definition of an Apex policy. A policy is made up of a tree of states, each represented by an {@link AxState} instance. The states of a
 * policy are held in a map in the policy. The state tree is built up at policy design time by a policy editor and each state is connected to its next state by
 * an {@link AxStateOutput} instance.
 * <p>
 * Execution of a policy is triggered by an event. A policy starts execution from its first state so the trigger event for the first sate is the trigger event
 * for the entire policy. Execution from that first state can continue to one or more subsequent states and so on down branches of states. The state output of
 * the final state in a branch has no next state, indicating the end of execution of that branch. Therefore, the set of output events from final states in the
 * policy are the possible set of output events on the policy. A state may only be used once in the state tree of a policy and recursive execution of states in
 * the same execution branch is not allowed, so the same state may not execute more than once on a single execution of a policy.
 * <p>
 * The template of a policy is a string that can be used by policy editors to store meta information on the policy that can be used at design time. The policy
 * template string is not used during policy execution.
 * <p>
 * During validation of a policy, the validation checks listed below are executed:
 * <ol>
 * <li>The policy key must not be a null key
 * <li>The policy key must be valid
 * <li>If the policy template is not set, an observation is issued
 * <li>At least one state must be defined
 * <li>Keys and values must all be defined, that is not null
 * <li>The key on each entry in the state map must match the key in the entry's value
 * <li>The parent key of each state in the state map of a policy must be the key of that policy
 * <li>Each state must itself be valid, see validation in {@link AxState}
 * <li>The next state of the state output of each state must be defined as a state in the policy
 * <li>The first state of a policy must be set
 * <li>The first state of a policy must be defined in the policy
 * <li>If a state is defined but is not used in a policy,a warning is issued
 * <li>The state tree of the policy must be valid, see validation in {@link AxStateTree}
 * </ol>
 */

@Entity
@Table(name = "AxPolicy")

@XmlRootElement(name = "apexPolicy", namespace = "http://www.ericsson.com/apex")

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AxPolicy", namespace = "http://www.ericsson.com/apex", propOrder = { "key", "template", "stateMap", "firstState" })

public class AxPolicy extends AxConcept {
    private static final long serialVersionUID = -1775614096390365941L;

    @EmbeddedId
    @XmlElement(name = "policyKey", required = true)
    private AxArtifactKey key;

    @Column(name = "template")
    @XmlElement(required = true)
    private String template;

    // @formatter:off
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(joinColumns = {
            @JoinColumn(name = "parentKeyName", referencedColumnName = "name"),
            @JoinColumn(name = "parentKeyVersion", referencedColumnName = "version")
    })
    @XmlElement(name = "state", required = true)
    private Map<String, AxState> stateMap;
    // @formatter:on

    @Column(name = "firstState")
    @XmlElement(required = true)
    private String firstState;

    /**
     * The Default Constructor creates a policy instance with a null key, a blank template and undefined first state.
     */
    public AxPolicy() {
        this(new AxArtifactKey());
    }

    /**
     * The Key Constructor creates a policy instance with the given key, a blank template and undefined first state.
     *
     * @param key the key of the policy
     */
    public AxPolicy(final AxArtifactKey key) {
        this(key, "", new TreeMap<String, AxState>(), "");
    }

    /**
     * This Constructor creates a policy with the given key and all its fields defined.
     *
     * @param key the key of the policy
     * @param template the policy template for policy editor metadata
     * @param stateMap the state map containing the states of the policy
     * @param firstState the first state that will execute on this policy
     */
    public AxPolicy(final AxArtifactKey key, final String template, final Map<String, AxState> stateMap, final String firstState) {
        super();
        Assertions.argumentNotNull(key, "key may not be null");
        Assertions.argumentNotNull(template, "template may not be null");
        Assertions.argumentNotNull(stateMap, "stateMap may not be null");
        Assertions.argumentNotNull(firstState, "firstState may not be null");

        this.key = key;
        this.template = template;
        this.stateMap = stateMap;
        this.firstState = firstState;
    }

    /**
     * Gets a tree that holds all the possible execution paths for this policy. This method may be used for verification of policies, to find the branches of
     * policy execution and the final states of policies.
     *
     * @return the state tree of the policy, a tree representing the execution branches of the policy
     */
    public AxStateTree getStateTree() {
        return new AxStateTree(this, stateMap.get(firstState), null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#getKey()
     */
    @Override
    public AxArtifactKey getKey() {
        return key;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#getKeys()
     */
    @Override
    public List<AxKey> getKeys() {
        final List<AxKey> keyList = key.getKeys();
        for (final AxState state : stateMap.values()) {
            keyList.addAll(state.getKeys());
        }
        return keyList;
    }

    /**
     * Sets the key of the policy.
     *
     * @param key the key of the policy
     */
    public void setKey(final AxArtifactKey key) {
        Assertions.argumentNotNull(key, "key may not be null");
        this.key = key;
    }

    /**
     * Gets the policy template for policy editor metadata.
     *
     * @return the policy template for policy editor metadata
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Sets the policy template for policy editor metadata.
     *
     * @param template the policy template for policy editor metadata
     */
    public void setTemplate(final String template) {
        Assertions.argumentNotNull(template, "template may not be null");
        this.template = template;
    }

    /**
     * Gets a map containing the states of the policy.
     *
     * @return the map of states in the policy
     */
    public Map<String, AxState> getStateMap() {
        return stateMap;
    }

    /**
     * Sets a map containing the states of the policy.
     *
     * @param stateMap a map of states in the policy
     */
    public void setStateMap(final Map<String, AxState> stateMap) {
        Assertions.argumentNotNull(stateMap, "stateMap may not be null");
        this.stateMap = stateMap;
    }

    /**
     * Gets the first state of the policy.
     *
     * @return the first state of the policy
     */
    public String getFirstState() {
        return firstState;
    }

    /**
     * Sets the first state of the policy.
     *
     * @param firstState the first state of the policy
     */
    public void setFirstState(final String firstState) {
        Assertions.argumentNotNull(firstState, "firstState may not be null");
        this.firstState = firstState;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#validate(com.ericsson.apex.model.basicmodel.concepts.AxValidationResult)
     */
    @Override
    public AxValidationResult validate(final AxValidationResult resultIn) {
        AxValidationResult result = resultIn;

        if (key.equals(AxArtifactKey.getNullKey())) {
            result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "key is a null key"));
        }

        result = key.validate(result);

        if (template.trim().length() == 0) {
            result.addValidationMessage(
                    new AxValidationMessage(key, this.getClass(), ValidationResult.OBSERVATION, "a policy template has not been specified"));
        }

        if (stateMap.size() == 0) {
            result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "stateMap may not be empty"));
        }
        else {
            for (final Entry<String, AxState> stateEntry : stateMap.entrySet()) {
                if (stateEntry.getKey() == null || stateEntry.getKey().equals(AxKey.NULL_KEY_NAME)) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                            "key on state entry key " + stateEntry.getKey() + " may not be the null key"));
                    continue;
                }

                if (stateEntry.getValue() == null) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                            "value on state entry value " + stateEntry.getKey() + " may not be null"));
                    continue;
                }

                if (!stateEntry.getKey().equals(stateEntry.getValue().getKey().getLocalName())) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "key on state entry key "
                            + stateEntry.getKey() + " does not equal state entry value local name " + stateEntry.getValue().getKey().getLocalName()));
                }

                if (!stateEntry.getValue().getKey().getParentArtifactKey().equals(key)) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                            "parent key on state entry key " + stateEntry.getValue().getKey() + " does not equal policy key"));
                }

                result = stateEntry.getValue().validate(result);

                for (final AxStateOutput stateOutput : stateEntry.getValue().getStateOutputs().values()) {
                    if (!stateOutput.getNextState().equals(AxReferenceKey.getNullKey()) && !stateMap.containsKey(stateOutput.getNextState().getLocalName())) {
                        result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                                " nextState of state " + stateEntry.getKey() + " not found in StateMap: " + stateOutput.getNextState().getID()));
                    }
                }
            }
        }

        // Validation continues from this point only if all validation checks this far have been passed
        if (!result.isOK()) {
            return result;
        }
        
        // We only check the unused states on models validated this far
        if (firstState.trim().length() == 0) {
            result.addValidationMessage(
                    new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "no first state specified, first state may not be blank"));
        }
        else {
            if (!stateMap.containsKey(firstState)) {
                result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "first state not found in stateMap"));
            }
            else {
                try {
                    // COnstructor validates policy state tree
                    AxStateTree policyStateTree = getStateTree();

                    // Check for unused states
                    final Set<AxState> referencedStateSet = policyStateTree.getReferencedStateSet();
                    final Set<AxState> unreferencedStateSet = new TreeSet<>(stateMap.values());
                    unreferencedStateSet.removeAll(referencedStateSet);

                    for (final AxState unreferencedState : unreferencedStateSet) {
                        result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.WARNING,
                                "state " + unreferencedState.getKey() + " is not referenced in the policy execution tree"));
                    }
                }
                catch (PolicyRuntimeException pre) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.WARNING,
                            "state tree in policy is invalid"));
                }
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#clean()
     */
    @Override
    public void clean() {
        key.clean();
        for (final Entry<String, AxState> stateEntry : stateMap.entrySet()) {
            stateEntry.getKey().trim();
            stateEntry.getValue().clean();
        }
        firstState.trim();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(":(");
        builder.append("key=");
        builder.append(key);
        builder.append(",template=");
        builder.append(template);
        builder.append(",stateMap=");
        builder.append(stateMap);
        builder.append(",firstState=");
        builder.append(firstState);
        builder.append(")");
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#clone()
     */
    @Override
    public Object clone() {
        return copyTo(new AxPolicy());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#copyTo(java.lang.Object)
     */
    @Override
    public Object copyTo(final Object target) {
        Assertions.argumentNotNull(target, "target may not be null");

        final Object copyObject = target;
        Assertions.instanceOf(copyObject, AxPolicy.class);

        final AxPolicy copy = ((AxPolicy) copyObject);
        copy.setKey((AxArtifactKey) key.clone());
        copy.setTemplate(new String(template));

        final Map<String, AxState> newStateMap = new TreeMap<>();
        for (final Entry<String, AxState> stateMapEntry : stateMap.entrySet()) {
            newStateMap.put(new String(stateMapEntry.getKey()), (AxState) stateMapEntry.getValue().clone());
        }
        copy.setStateMap(newStateMap);

        copy.setFirstState(new String(firstState));

        return copyObject;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + key.hashCode();
        result = prime * result + template.hashCode();
        result = prime * result + stateMap.hashCode();
        result = prime * result + firstState.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final AxPolicy other = (AxPolicy) obj;
        if (!key.equals(other.key)) {
            return false;
        }
        if (!template.equals(other.template)) {
            return false;
        }
        if (!stateMap.equals(other.stateMap)) {
            return false;
        }
        return firstState.equals(other.firstState);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final AxConcept otherObj) {
        if (otherObj == null) {
            return -1;
        }
        if (this == otherObj) {
            return 0;
        }
        if (getClass() != otherObj.getClass()) {
            return this.hashCode() - otherObj.hashCode();
        }

        final AxPolicy other = (AxPolicy) otherObj;
        if (!key.equals(other.key)) {
            return key.compareTo(other.key);
        }
        if (!template.equals(other.template)) {
            return template.compareTo(other.template);
        }
        if (!stateMap.equals(other.stateMap)) {
            return (stateMap.hashCode() - other.stateMap.hashCode());
        }
        return firstState.compareTo(other.firstState);
    }
}

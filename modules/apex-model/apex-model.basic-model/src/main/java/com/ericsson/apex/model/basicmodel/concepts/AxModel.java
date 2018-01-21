/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.concepts;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class is the base class for all models in Apex. All model classes inherit from this model so all models must have a key and have key information.
 * <p>
 * Validation checks that the model key is valid. It goes on to check for null keys and checks each key for uniqueness in the model. A check is carried out to
 * ensure that an {@link AxKeyInfo} instance exists for every {@link AxArtifactKey} key. For each {@link AxReferenceKey} instance, a check is made that its
 * parent and local name are nut null and that a {@link AxKeyInfo} entry exists for its parent. Then a check is made that each used {@link AxArtifactKey} and
 * {@link AxReferenceKey} usage references a key that exists. Finally, a check is made to ensure that an {@link AxArtifactKey} instance exists for every
 * {@link AxKeyInfo} instance.
 */

@Entity
@Table(name = "AxModel")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@XmlRootElement(name = "apexModel", namespace = "http://www.ericsson.com/apex")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AxModel", namespace = "http://www.ericsson.com/apex", propOrder = { "key", "keyInformation" })

public class AxModel extends AxConcept {
    private static final long serialVersionUID = -771659065637205430L;

    @EmbeddedId
    @XmlElement(name = "key", required = true)
    private AxArtifactKey key;

    // @formatter:off
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "keyInformationName", referencedColumnName = "name"),
        @JoinColumn(name = "keyInformationVersion", referencedColumnName = "version")
    })
    @XmlElement(name = "keyInformation", required = true)
    private AxKeyInformation keyInformation;
    // @formatter:on

    /**
     * The Default Constructor creates this concept with a NULL artifact key.
     */
    public AxModel() {
        this(new AxArtifactKey());
    }

    /**
     * Constructor to create this concept with the specified key.
     *
     * @param key the key of this concept
     */
    public AxModel(final AxArtifactKey key) {
        this(key, new AxKeyInformation(new AxArtifactKey(key.getName() + "_KeyInfo", key.getVersion())));
    }

    /**
     * Constructor to create this concept and set all its fields.
     *
     * @param key the key of this concept
     * @param keyInformation the key information of this concept
     */
    public AxModel(final AxArtifactKey key, final AxKeyInformation keyInformation) {
        super();
        Assertions.argumentNotNull(key, "key may not be null");
        Assertions.argumentNotNull(keyInformation, "keyInformation may not be null");

        this.key = key;
        this.keyInformation = keyInformation;
    }

    /**
     * Registers this model with the {@link ModelService}. All models are registered with the model service so that models can be references from anywhere in
     * the Apex system without being passed as references through deep call chains.
     */
    public void register() {
        ModelService.registerModel(AxKeyInformation.class, getKeyInformation());
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
        final List<AxKey> keyList = (List<AxKey>) key.getKeys();

        // We just add the key for the KeyInformation itself. We don't add the
        // keys from key information because
        // that is a list of key information for existing keys
        keyList.add(keyInformation.getKey());

        return keyList;
    }

    /**
     * Sets the key of this concept.
     *
     * @param key the key of this concept
     */
    public void setKey(final AxArtifactKey key) {
        Assertions.argumentNotNull(key, "key may not be null");
        this.key = key;
    }

    /**
     * Gets the key information of this concept.
     *
     * @return the key information of this concept
     */
    public AxKeyInformation getKeyInformation() {
        return keyInformation;
    }

    /**
     * Sets the key information of this concept.
     *
     * @param keyInformation the key information of this concept
     */
    public void setKeyInformation(final AxKeyInformation keyInformation) {
        Assertions.argumentNotNull(keyInformation, "keyInformation may not be null");
        this.keyInformation = keyInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#validate(com. ericsson.apex.model.basicmodel.concepts.AxValidationResult)
     */
    @Override
    public AxValidationResult validate(final AxValidationResult resultIn) {
        AxValidationResult result = resultIn;

        if (key.equals(AxArtifactKey.getNullKey())) {
            result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "key is a null key"));
        }

        result = key.validate(result);
        result = keyInformation.validate(result);

        // Key consistency check
        final Set<AxArtifactKey> artifactKeySet = new TreeSet<>();
        final Set<AxReferenceKey> referenceKeySet = new TreeSet<>();
        final Set<AxKeyUse> usedKeySet = new TreeSet<>();

        for (final AxKey axKey : this.getKeys()) {
            // Check for the two type of keys we have
            if (axKey instanceof AxArtifactKey) {
                final AxArtifactKey artifactKey = (AxArtifactKey) axKey;

                // Null key check
                if (artifactKey.equals(AxArtifactKey.getNullKey())) {
                    result.addValidationMessage(
                            new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "key " + artifactKey + " is a null key"));
                }

                // Null key name start check
                if (artifactKey.getName().toUpperCase().startsWith(AxKey.NULL_KEY_NAME)) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                            "key " + artifactKey + " name starts with keyword " + AxKey.NULL_KEY_NAME));
                }

                // Unique key check
                if (artifactKeySet.contains(artifactKey)) {
                    result.addValidationMessage(
                            new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "duplicate key " + artifactKey + " found"));
                }
                else {
                    artifactKeySet.add(artifactKey);
                }

                // Key information check
                if (!keyInformation.getKeyInfoMap().containsKey(artifactKey)) {
                    result.addValidationMessage(
                            new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "key information not found for key " + artifactKey));
                }
            }
            else if (axKey instanceof AxReferenceKey) {
                final AxReferenceKey referenceKey = (AxReferenceKey) axKey;

                // Null key check
                if (referenceKey.equals(AxReferenceKey.getNullKey())) {
                    result.addValidationMessage(
                            new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "key " + referenceKey + " is a null key"));
                }

                // Null parent key check
                if (referenceKey.getParentArtifactKey().equals(AxArtifactKey.getNullKey())) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                            "parent artifact key of key " + referenceKey + " is a null key"));
                }

                // Null local name check
                if (referenceKey.getLocalName().equals(AxKey.NULL_KEY_NAME)) {
                    result.addValidationMessage(
                            new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "key " + referenceKey + " has a null local name"));
                }

                // Null key name start check
                if (referenceKey.getParentArtifactKey().getName().toUpperCase().startsWith(AxKey.NULL_KEY_NAME)) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                            "key " + referenceKey + " parent name starts with keyword " + AxKey.NULL_KEY_NAME));
                }

                // Unique key check
                if (referenceKeySet.contains(referenceKey)) {
                    result.addValidationMessage(
                            new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "duplicate key " + referenceKey + " found"));
                }
                else {
                    referenceKeySet.add(referenceKey);
                }

                // Key information check
                if (!keyInformation.getKeyInfoMap().containsKey(referenceKey.getParentArtifactKey())) {
                    result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                            "key information not found for parent key of key " + referenceKey));
                }
            }
            // It must be an AxKeyUse, nothing else is legal
            else {
                usedKeySet.add((AxKeyUse) axKey);
            }
        }

        // Check all reference keys have correct parent keys
        for (final AxReferenceKey referenceKey : referenceKeySet) {
            if (!artifactKeySet.contains(referenceKey.getParentArtifactKey())) {
                result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID,
                        "parent artifact key not found for reference key " + referenceKey));
            }
        }

        // Check all key uses
        for (final AxKeyUse usedKey : usedKeySet) {
            if (usedKey.getKey() instanceof AxArtifactKey) {
                // AxArtifact key usage, check the key exists
                if (!artifactKeySet.contains(usedKey.getKey())) {
                    result.addValidationMessage(new AxValidationMessage(usedKey.getKey(), this.getClass(), ValidationResult.INVALID,
                            "an artifact key used in the model is not defined"));
                }
            }
            else {
                // AxReference key usage, check the key exists
                if (!referenceKeySet.contains(usedKey.getKey())) {
                    result.addValidationMessage(new AxValidationMessage(usedKey.getKey(), this.getClass(), ValidationResult.INVALID,
                            "a reference key used in the model is not defined"));
                }
            }
        }

        // Check key information for unused key information
        for (final AxArtifactKey keyInfoKey : keyInformation.getKeyInfoMap().keySet()) {
            if (!artifactKeySet.contains(keyInfoKey)) {
                result.addValidationMessage(
                        new AxValidationMessage(keyInfoKey, this.getClass(), ValidationResult.WARNING, "key not found for key information entry"));
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
        keyInformation.clean();
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
        builder.append(",keyInformation=");
        builder.append(keyInformation);
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
        return copyTo(new AxModel());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#copyTo(java.lang. Object)
     */
    @Override
    public Object copyTo(final Object target) {
        Assertions.argumentNotNull(target, "target may not be null");

        final Object copyObject = target;
        Assertions.instanceOf(copyObject, AxModel.class);

        final AxModel copy = ((AxModel) copyObject);
        copy.setKey((AxArtifactKey) key.clone());
        copy.setKeyInformation((AxKeyInformation) keyInformation.clone());

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
        result = prime * result + keyInformation.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#equals(java.lang. Object)
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

        final AxModel other = (AxModel) obj;
        if (!key.equals(other.key)) {
            return false;
        }
        return keyInformation.equals(other.keyInformation);
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

        final AxModel other = (AxModel) otherObj;
        if (!key.equals(other.key)) {
            return key.compareTo(other.key);
        }
        return keyInformation.compareTo(other.keyInformation);
    }
}

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

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;

/**
 * This class holds Task Logic for {@link AxTask} tasks in Apex. It is a specialization of the {@link AxLogic} class, so that Task Logic in Apex states can be
 * strongly typed.
 * <p>
 * Task Logic is used to execute tasks {@link AxTask} in Apex. The logic uses fields on the incoming trigger event and information from the context albums
 * available on a task to get context during execution. The task logic populates the output fields of the task.
 * <p>
 * Validation uses standard Apex Logic validation, see validation in {@link AxLogic}.
 */
@Entity
@Table(name = "AxTaskLogic")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "apexLogic", namespace = "http://www.ericsson.com/apex")
@XmlType(name = "AxTaskLogic", namespace = "http://www.ericsson.com/apex")

public class AxTaskLogic extends AxLogic {
    private static final long serialVersionUID = 2090324845463750391L;

    /**
     * The Default Constructor creates a logic instance with a null key, undefined logic flavour and a null logic string.
     */
    public AxTaskLogic() {
        super();
    }

    /**
     * The Key Constructor creates a logic instance with the given reference key, undefined logic flavour and a null logic string.
     *
     * @param key the reference key of the logic
     */
    public AxTaskLogic(final AxReferenceKey key) {
        super(key, LOGIC_FLAVOUR_UNDEFINED, "");
    }

    /**
     * This Constructor creates a logic instance with a reference key constructed from the parents key and the logic local name and all of its fields defined.
     *
     * @param parentKey the reference key of the parent of this logic
     * @param logicName the logic name, held as the local name of the reference key of this logic
     * @param logicFlavour the flavour of this logic
     * @param logic the actual logic as a string
     */
    public AxTaskLogic(final AxArtifactKey parentKey, final String logicName, final String logicFlavour, final String logic) {
        super(new AxReferenceKey(parentKey, logicName), logicFlavour, logic);
    }

    /**
     * This Constructor creates a logic instance with the given reference key and all of its fields defined.
     *
     * @param key the reference key of this logic
     * @param logicFlavour the flavour of this logic
     * @param logic the actual logic as a string
     */
    public AxTaskLogic(final AxReferenceKey key, final String logicFlavour, final String logic) {
        super(key, logicFlavour, logic);
    }

    /**
     * This Constructor creates a logic instance by cloning the fields from another logic instance into this logic instance.
     *
     * @param logic the logic instance to clone from
     */
    public AxTaskLogic(final AxLogic logic) {
        super((AxReferenceKey) logic.getKey().clone(), new String(logic.getLogicFlavour()), new String(logic.getLogic()));
    }

    /**
     * This Constructor creates a logic instance with a reference key constructed from the parents key and the logic local name, the given logic flavour, with
     * the logic being provided by the given logic reader instance.
     *
     * @param parentKey the reference key of the parent of this logic
     * @param logicName the logic name, held as the local name of the reference key of this logic
     * @param logicFlavour the flavour of this logic
     * @param logicReader the logic reader to use to read the logic for this logic instance
     */
    public AxTaskLogic(final AxArtifactKey parentKey, final String logicName, final String logicFlavour, final AxLogicReader logicReader) {
        super(new AxReferenceKey(parentKey, logicName), logicFlavour, logicReader);
    }

    /**
     * This Constructor creates a logic instance with the given reference key and logic flavour, the logic is provided by the given logic reader instance.
     *
     * @param key the reference key of this logic
     * @param logicFlavour the flavour of this logic
     * @param logicReader the logic reader to use to read the logic for this logic instance
     */
    public AxTaskLogic(final AxReferenceKey key, final String logicFlavour, final AxLogicReader logicReader) {
        super(key, logicFlavour, logicReader);
    }
}

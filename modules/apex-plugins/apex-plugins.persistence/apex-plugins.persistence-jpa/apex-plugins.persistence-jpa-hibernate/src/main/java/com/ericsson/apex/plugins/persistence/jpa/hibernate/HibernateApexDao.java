/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.persistence.jpa.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao;

/**
 * The Class HibernateApexDao is the Hibernate JPA implementation of the Apex DAO.
 *
 * @author Sergey Sachkov (sergey.sachkov@ericsson.com)
 */
public class HibernateApexDao extends DefaultApexDao {
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(HibernateApexDao.class);
	
	private static final String FROM = "FROM ";
	private static final String DELETE_FROM = "DELETE FROM ";
	private static final String WHERE_KEY_NAME = " WHERE key.name='";
    private static final String AND_KEY_VERSION = "' AND key.version='";
	private static final String WHERE_KEY_PARENT_KEY_NAME = " WHERE key.parentKeyName='";
	private static final String AND_KEY_PARENT_KEY_VERSION = "' AND key.parentKeyVersion='";
	private static final String AND_KEY_LOCAL_NAME = "' AND key.localName='";

	/*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao#delete(java.lang.Class, com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey)
     */
    @Override
    public <T extends AxConcept> void delete(final Class<T> aClass, final AxArtifactKey key) {
        if (key == null) {
            return;
        }
        final EntityManager mg = getEntityManager();
        try {
            mg.getTransaction().begin();
            mg.createQuery(DELETE_FROM + aClass.getSimpleName() + WHERE_KEY_NAME + key.getName() + AND_KEY_VERSION + key.getVersion() + "'")
                    .executeUpdate();
            mg.getTransaction().commit();
        }
        finally {
            mg.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao#delete(java.lang.Class, com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey)
     */
    @Override
    public <T extends AxConcept> void delete(final Class<T> aClass, final AxReferenceKey key) {
        if (key == null) {
            return;
        }
        final EntityManager mg = getEntityManager();
        try {
            mg.getTransaction().begin();
            mg.createQuery(DELETE_FROM + aClass.getSimpleName() + WHERE_KEY_PARENT_KEY_NAME + key.getParentKeyName() + AND_KEY_PARENT_KEY_VERSION
                    + key.getParentKeyVersion() + AND_KEY_LOCAL_NAME + key.getLocalName() + "'").executeUpdate();
            mg.getTransaction().commit();
        }
        finally {
            mg.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao#deleteByArtifactKey(java.lang.Class, java.util.Collection)
     */
    @Override
    public <T extends AxConcept> int deleteByArtifactKey(final Class<T> aClass, final Collection<AxArtifactKey> keys) {
        if (keys == null || keys.isEmpty()) {
            return 0;
        }
        int deletedCount = 0;
        final EntityManager mg = getEntityManager();
        try {
            mg.getTransaction().begin();
            for (final AxArtifactKey key : keys) {
                deletedCount += mg
                        .createQuery(
                                DELETE_FROM + aClass.getSimpleName() + WHERE_KEY_NAME + key.getName() + AND_KEY_VERSION + key.getVersion() + "'")
                        .executeUpdate();
            }
            mg.getTransaction().commit();
        }
        finally {
            mg.close();
        }
        return deletedCount;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.dao.ApexDao#deleteByContextUsageKey(java.lang.Class, java.util.Collection)
     */
    @Override
    public <T extends AxConcept> int deleteByReferenceKey(final Class<T> aClass, final Collection<AxReferenceKey> keys) {
        if (keys == null || keys.isEmpty()) {
            return 0;
        }
        int deletedCount = 0;
        final EntityManager mg = getEntityManager();
        try {
            mg.getTransaction().begin();
            for (final AxReferenceKey key : keys) {
                deletedCount += mg
                        .createQuery(DELETE_FROM + aClass.getSimpleName() + WHERE_KEY_PARENT_KEY_NAME + key.getParentKeyName()
                                + AND_KEY_PARENT_KEY_VERSION + key.getParentKeyVersion() + AND_KEY_LOCAL_NAME + key.getLocalName() + "'")
                        .executeUpdate();
            }
            mg.getTransaction().commit();
        }
        finally {
            mg.close();
        }
        return deletedCount;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.dao.ApexDao#deleteAll(java.lang.Class)
     */
    @Override
    public <T extends AxConcept> void deleteAll(final Class<T> aClass) {
        final EntityManager mg = getEntityManager();
        try {
            mg.getTransaction().begin();
            mg.createQuery(DELETE_FROM + aClass.getSimpleName()).executeUpdate();
            mg.getTransaction().commit();
        }
        finally {
            mg.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.dao.ApexDao#getAll(java.lang.Class)
     */
    @Override
    public <T extends AxConcept> List<T> getAll(final Class<T> aClass) {
        if (aClass == null) {
            return Collections.emptyList();
        }
        final EntityManager mg = getEntityManager();
        try {
            final List<T> result = mg.createQuery(FROM + aClass.getSimpleName(), aClass).getResultList();
            final List<T> cloneResult = new ArrayList<>();
            for (final T t : result) {
				try { 
					T clonedT = aClass.newInstance();
					t.copyTo(clonedT);
	                cloneResult.add(clonedT);
				}
				catch (Exception e) {
					LOGGER.warn("Could not clone object of class \"" + aClass.getCanonicalName() + "\"", e);
					return cloneResult;
				}
            }
            return cloneResult;
        }
        finally {
            mg.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.dao.ApexDao#getAll(java.lang.Class, com.ericsson.apex.core.model.concepts.AxArtifactKey)
     */
    @Override
    public <T extends AxConcept> List<T> getAll(final Class<T> aClass, final AxArtifactKey parentKey) {
        if (aClass == null) {
            return Collections.emptyList();
        }
        final EntityManager mg = getEntityManager();
        try {
            return mg.createQuery(FROM + aClass.getSimpleName() + WHERE_KEY_PARENT_KEY_NAME + parentKey.getName() + AND_KEY_PARENT_KEY_VERSION
                    + parentKey.getVersion() + "'", aClass).getResultList();
        }
        finally {
            mg.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.dao.ApexDao#getArtifact(java.lang.Class, com.ericsson.apex.core.model.concepts.AxArtifactKey)
     */
    @Override
    public <T extends AxConcept> T getArtifact(final Class<T> aClass, final AxArtifactKey key) {
        if (aClass == null || key == null) {
            return null;
        }
        final EntityManager mg = getEntityManager();
        List<T> ret;
        try {
            ret = mg.createQuery(FROM + aClass.getSimpleName() + WHERE_KEY_NAME + key.getName() + AND_KEY_VERSION + key.getVersion() + "'",
                    aClass).getResultList();
        }
        finally {
            mg.close();
        }
        if (ret == null || ret.isEmpty()) {
            return null;
        }
        if (ret.size() > 1) {
            throw new IllegalArgumentException("More than one result was returned for search for " + aClass + " with key " + key.getID() + ": " + ret);
        }
        return ret.get(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.core.model.dao.ApexDao#getArtifact(java.lang.Class, com.ericsson.apex.core.model.concepts.AxReferenceKey)
     */
    @Override
    public <T extends AxConcept> T getArtifact(final Class<T> aClass, final AxReferenceKey key) {
        if (aClass == null || key == null) {
            return null;
        }
        final EntityManager mg = getEntityManager();
        List<T> ret;
        try {
            ret = mg.createQuery(FROM + aClass.getSimpleName() + WHERE_KEY_PARENT_KEY_NAME + key.getParentKeyName() + AND_KEY_PARENT_KEY_VERSION
                    + key.getParentKeyVersion() + AND_KEY_LOCAL_NAME + key.getLocalName() + "'", aClass).getResultList();
        }
        finally {
            mg.close();
        }
        if (ret == null || ret.isEmpty()) {
            return null;
        }
        if (ret.size() > 1) {
            throw new IllegalArgumentException("More than one result was returned for search for " + aClass + " with key " + key.getID() + ": " + ret);
        }
        return ret.get(0);
    }
}

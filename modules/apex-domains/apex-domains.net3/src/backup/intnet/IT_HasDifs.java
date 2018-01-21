/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/


package com.ericsson.apex.domains.net3.intnet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;

/**
 * A IT object that has DIFs.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_HasDifs extends IT_BaseObject {

	/** The normal DIFs in the network, generated from nodes. */
	protected final Map<String, IT_NormalDif> normalDifs;

	/** The Point-to-Point DIFs in the network, generated from nodes. */
	protected final Map<String, IT_PtpDif> ptpDifs;

	/**
	 * Creates a new intend network.
	 * @param name the network name, must not be blank
	 * @param version the network version, must not be blank
	 * @param nameSpace the network name space, must not be blank
	 * @param description a description, must not be blank
	 */
	protected IT_HasDifs(String name, String version, String nameSpace, String description){
		super(name, version, nameSpace, description);
		Validate.notBlank(description, "IT object <" + name + "> has blank description");

		this.normalDifs = new TreeMap<>();
		this.ptpDifs = new TreeMap<>();
	}

	/**
	 * Adds an array of DIFs to the local maps.
	 * @param difs array of DIFs, must not have null elements
	 * @param ignoreExisting false to validate existence of DIF, true otherwise
	 */
	public void addAllDifs(IT_Dif[] difs, boolean ignoreExisting){
		Validate.noNullElements(difs);
		for(IT_Dif dif : difs){
			this.addDif(dif, ignoreExisting);
		}
	}

	/**
	 * Adds a DIF to the local maps.
	 * @param dif the DIF to add, must not be null and not exist in local maps
	 * @param ignoreExisting false to validate existence of DIF, true otherwise
	 */
	public void addDif(IT_Dif dif, boolean ignoreExisting){
		Validate.notNull(dif);
		if(dif instanceof IT_NormalDif){
			IT_NormalDif ndif = (IT_NormalDif)dif;
			if(ignoreExisting==false){
				Validate.validState(!this.normalDifs.containsKey(ndif.identity.name), "HasDifs <" + this.identity.name + "> createNormalDif(): DIF <" + ndif.identity.name + "> already exists");
			}
			this.normalDifs.put(ndif.identity.name, ndif);
		}
		else if(dif instanceof IT_PtpDif){
			IT_PtpDif pdif = (IT_PtpDif)dif;
			if(ignoreExisting==false){
				Validate.validState(!this.ptpDifs.containsKey(pdif.identity.name), "HasDifs <" + this.identity.name + "> createNormalDif(): DIF <" + pdif.identity.name + "> already exists");
			}
			this.ptpDifs.put(pdif.identity.name, pdif);
		}
		else{
			throw new IllegalStateException("HasDifs: unknown DIF class: " + dif.getClass().getName());
		}
	}

	/**
	 * Returns a set of all DIFs.
	 * @return set of all DIFs
	 */
	public Set<IT_Dif> getAllDifs(){
		Set<IT_Dif> ret = new HashSet<>();
		ret.addAll(this.normalDifs.values());
		ret.addAll(this.ptpDifs.values());
		return ret;
	}

	/**
	 * Returns a DIF.
	 * @param name the name of the DIF
	 * @return DIf if found, null if not found
	 */
	public IT_Dif getDif(String name){
		IT_Dif ret = this.ptpDifs.get(name);
		if(ret==null){
			ret = this.normalDifs.get(name);
		}
		return ret;
	}

	/**
	 * Returns a normal DIF.
	 * @param name the name of the normal DIF
	 * @return DIf if found, null if not found
	 */
	public IT_NormalDif getNormalDif(String name){
		return this.normalDifs.get(name);
	}

	/**
	 * Returns all normal DIFs.
	 * @return all normal DIFs
	 */
	public Map<String, IT_NormalDif> getNormalDifs(){
		return this.normalDifs;
	}

	/**
	 * Returns a Point-to-Point DIF.
	 * @param name the name of the Point-to-Point DIF
	 * @return DIf if found, null if not found
	 */
	public IT_PtpDif getPtpDif(String name){
		return this.ptpDifs.get(name);
	}

	/**
	 * Returns all Point-to-Point DIFs.
	 * @return all Point-to-Point DIFs
	 */
	public Map<String, IT_PtpDif> getPtpDifs(){
		return this.ptpDifs;
	}
}

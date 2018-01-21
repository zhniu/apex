/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.intentnetwork

import com.ericsson.apex.domains.net3.basics.Net3Identity
import com.ericsson.apex.domains.net3.location.LocationMonitor
import java.util.HashSet
import java.util.Map
import java.util.Set
import java.util.TreeMap
import java.util.Vector
import org.eclipse.xtend.lib.annotations.Data

@Data class ITNode extends Net3Identity {

	Map<String, ITDif> normalDifs
	Map<String, ITPtpDif> ptpDifs

	Map<ITDif, ITDif[]> registrations = new TreeMap
	Map<String, ITLinkList> ieCount = new TreeMap

	Vector<String> topoSort

	LocationMonitor locations

	/** 
	 * Returns a set of all DIFs.
	 * @return set of all DIFs
	 */
	def Set<ITDif> getAllDifs() {
		var Set<ITDif> ret = new HashSet() 
		ret.addAll(this.normalDifs.values()) 
		ret.addAll(this.ptpDifs.values()) 
		return ret 
	}
}
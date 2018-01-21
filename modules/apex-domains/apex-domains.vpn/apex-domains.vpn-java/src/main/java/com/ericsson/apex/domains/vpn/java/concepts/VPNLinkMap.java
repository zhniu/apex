/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.vpn.java.concepts;

import java.util.HashMap;

/**
 * Class to wrap a link map to allow for schema generation of events using this type.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class VPNLinkMap extends HashMap<String, VPNLink> {
    private static final long serialVersionUID = -3752332382134673105L;
}

/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.utilities;

import org.junit.Test;

/**
 * Tests for {@link CliParser}.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class TestCliParser {

    /** Testapp version. */
    @Test
    public void testappVersion() {
        final CliParser cli = new CliParser();
        System.out.println(cli.getAppVersion());
    }
}

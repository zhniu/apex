/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.protocol.xml;

import java.util.Random;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class XMLEventGenerator {
	private static int nextEventNo = 0;

	public static String xmlEvents(final int eventCount) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < eventCount; i++) {
			if (i > 0) {
				builder.append("\n");
			}
			builder.append(xmlEvent());
		}

		return builder.toString();
	}

	public static String xmlEvent() {
		Random rand = new Random();

		StringBuilder builder = new StringBuilder();

		int nextEventNo = rand.nextInt(2);
		String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
		int nextMatchCase = rand.nextInt(4);
		float nextTestTemperature = rand.nextFloat() * 10000;

		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		builder.append("<xmlApexEvent xmlns=\"http://www.ericsson.com/apexevent\">\n");

		builder.append("  <name>" + eventName + "</name>\n");
		builder.append("  <version>0.0.1</version>\n");
		builder.append("  <nameSpace>com.ericsson.apex.sample.events</nameSpace>\n");
		builder.append("  <source>test</source>\n");
		builder.append("  <target>apex</target>\n");
		builder.append("  <data>\n");
		builder.append("    <key>TestSlogan</key>\n");
		builder.append("    <value>Test slogan for External Event" + (nextEventNo++) + "</value>\n");
		builder.append("  </data>\n");
		builder.append("  <data>\n");
		builder.append("    <key>TestMatchCase</key>\n");
		builder.append("	<value>" + nextMatchCase + "</value>\n");
		builder.append("  </data>\n");
		builder.append("  <data>\n");
		builder.append("    <key>TestTimestamp</key>\n");
		builder.append("    <value>" + System.currentTimeMillis() + "</value>\n");
		builder.append("  </data>\n");
		builder.append("  <data>\n");
		builder.append("    <key>TestTemperature</key>\n");
		builder.append("    <value>" + nextTestTemperature + "</value>\n");
		builder.append("  </data>\n");
		builder.append("</xmlApexEvent>");

		return builder.toString();
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("usage EventGenerator #events");
			return;
		}

		int eventCount = 0;
		try {
			eventCount = Integer.parseInt(args[0]);
		}
		catch (Exception e) {
			System.err.println("usage EventGenerator #events");
			e.printStackTrace();
			return;
		}

		System.out.println(xmlEvents(eventCount));
	}
	
	public static int getNextEventNo() {
		return nextEventNo;
	}
}

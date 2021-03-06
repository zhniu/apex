/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.events;

import java.util.Random;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EventGenerator {
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

    public static String jsonEvents(final int eventCount) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < eventCount; i++) {
            if (i > 0) {
                builder.append("\n");
            }
            builder.append(jsonEvent());
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
        builder.append("    <value>" + nextMatchCase + "</value>\n");
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

    public static String jsonEvent() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNoName() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"namez\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventBadName() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"%%%%\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNoExName() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"I_DONT_EXIST\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNoVersion() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"versiion\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventBadVersion() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"#####\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNoExVersion() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"Event0000\",\n");
        builder.append("  \"version\": \"1.2.3\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNoNamespace() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpacee\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventBadNamespace() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"hello.&&&&\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNoExNamespace() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"pie.in.the.sky\",\n");
        builder.append("  \"name\": \"Event0000\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNoSource() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"sourcee\": \"test\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventBadSource() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"%!@**@!\",\n");
        builder.append("  \"target\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNoTarget() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"targett\": \"apex\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventBadTarget() {
        Random rand = new Random();

        StringBuilder builder = new StringBuilder();

        int nextEventNo = rand.nextInt(2);
        String eventName = (nextEventNo == 0 ? "Event0000" : "Event0100");
        int nextMatchCase = rand.nextInt(4);
        float nextTestTemperature = rand.nextFloat() * 10000;

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"" + eventName + "\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"KNIO(*S)A(S)D\",\n");
        builder.append("  \"TestSlogan\": \"Test slogan for External Event" + (nextEventNo++) + "\",\n");
        builder.append("  \"TestMatchCase\": " + nextMatchCase + ",\n");
        builder.append("  \"TestTimestamp\": " + System.currentTimeMillis() + ",\n");
        builder.append("  \"TestTemperature\": " + nextTestTemperature + "\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventMissingFields() {
        StringBuilder builder = new StringBuilder();

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"Event0000\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"apex\"\n");
        builder.append("}");

        return builder.toString();
    }

    public static String jsonEventNullFields() {
        StringBuilder builder = new StringBuilder();

        builder.append("{\n");
        builder.append("  \"nameSpace\": \"com.ericsson.apex.sample.events\",\n");
        builder.append("  \"name\": \"Event0000\",\n");
        builder.append("  \"version\": \"0.0.1\",\n");
        builder.append("  \"source\": \"test\",\n");
        builder.append("  \"target\": \"Apex\",\n");
        builder.append("  \"TestSlogan\": null,\n");
        builder.append("  \"TestMatchCase\": -1,\n");
        builder.append("  \"TestTimestamp\": -1,\n");
        builder.append("  \"TestTemperature\": -1.0\n");
        builder.append("}");

        return builder.toString();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("usage EventGenerator #events XML|JSON");
            return;
        }

        int eventCount = 0;
        try {
            eventCount = Integer.parseInt(args[0]);
        }
        catch (Exception e) {
            System.err.println("usage EventGenerator #events XML|JSON");
            e.printStackTrace();
            return;
        }

        if (args[1].equalsIgnoreCase("XML")) {
            System.out.println(xmlEvents(eventCount));
        }
        else if (args[1].equalsIgnoreCase("JSON")) {
            System.out.println(jsonEvents(eventCount));
        }
        else {
            System.err.println("usage EventGenerator #events XML|JSON");
            return;
        }
    }
    
    public static int getNextEventNo() {
        return nextEventNo;
    }
}

/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import com.ericsson.apex.services.client.editor.rest.ApexEditorMain;
import com.ericsson.apex.services.client.editor.rest.ApexEditorMain.EditorState;

/**
 * The Class TestApexEditorStartup.
 */
public class TestApexEditorStartup {
    //CHECKSTYLE:OFF: MagicNumber

    /** Test no args.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testNoArgs() throws IOException, InterruptedException {
        String[] args = new String[] {};

        String outString = runEditor(args);
        assertTrue(outString.startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:18989/apexservices/, TTL=-1sec], "
                + "State=READY) starting at http://0.0.0.0:18989/apexservices/"));
        assertTrue(outString.contains("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:18989/apexservices/, TTL=-1sec], "
                + "State=RUNNING) started at http://0.0.0.0:18989/apexservices/"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").endsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:18989/apexservices/, TTL=-1sec], State=STOPPED) shut down "));
    }

    /** Test bad arg 0.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testBadArg0() throws IOException, InterruptedException {
        String[] args = new String[] {"12321"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getLocalizedMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[null], State=STOPPED) parameter error, too many command line arguments specified : [12321]"));
        }
    }

    /** Test bad arg 1.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testBadArg1() throws IOException, InterruptedException {
        String[] args = new String[] {"12321 12322 12323"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getLocalizedMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[null], State=STOPPED) parameter error, too many command line arguments specified : [12321 12322 12323]"));
        }
    }

    /** Test bad arg 2.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testBadArg2() throws IOException, InterruptedException {
        String[] args = new String[] {"-z"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getLocalizedMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[null], State=STOPPED) parameter error, invalid command line arguments specified : Unrecognized option: -z"));
        }
    }

    /** Test bad arg 3.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testBadArg3() throws IOException, InterruptedException {
        String[] args = new String[] {"--hello"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getLocalizedMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[null], State=STOPPED) parameter error, invalid command line arguments specified : Unrecognized option: --hello"));
        }
    }


    /** Test bad arg 4.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testBadArg4() throws IOException, InterruptedException {
        String[] args = new String[] {"-l", "+++++"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getLocalizedMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[ApexEditorParameters: URI=http://+++++:18989/apexservices/, TTL=-1sec], "
                    + "State=STOPPED) parameters invalid, listen address is not valid. "
                    + "Illegal character in hostname at index 7: http://+++++:18989/apexservices/"));
            }
    }

    /** Test help 0.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testHelp0() throws IOException, InterruptedException {
        String[] args = new String[] {"--help"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("usage: com.ericsson.apex.services.client.editor.rest.ApexEditorMain [options...]"));
        }
    }

    /** Test help 1.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testHelp1() throws IOException, InterruptedException {
        String[] args = new String[] {"-h"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("usage: com.ericsson.apex.services.client.editor.rest.ApexEditorMain [options...]"));
        }
    }

    /** Test port arg.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testPortArgShJo() throws IOException, InterruptedException {
        String[] args = new String[] {"-p12321"};

        String outString = runEditor(args);

        assertTrue(outString.startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=-1sec], "
                + "State=READY) starting at http://0.0.0.0:12321/apexservices/"));
        assertTrue(outString.contains("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=-1sec], "
                + "State=RUNNING) started at http://0.0.0.0:12321/apexservices/"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").endsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=-1sec], State=STOPPED) shut down "));
    }

    /** Test port arg.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testPortArgShSe() throws IOException, InterruptedException {
        String[] args = new String[] {"-p", "12321"};

        String outString = runEditor(args);

        assertTrue(outString.startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=-1sec], "
                + "State=READY) starting at http://0.0.0.0:12321/apexservices/"));
        assertTrue(outString.contains("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=-1sec], "
                + "State=RUNNING) started at http://0.0.0.0:12321/apexservices/"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").endsWith("(ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=-1sec], State=STOPPED) shut down "));
    }


    /** Test port arg.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testPortArgSpace() throws IOException, InterruptedException {
        String[] args = new String[] {"-p 12321"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[null], State=STOPPED) parameter error, error parsing argument \"port\" :For input string: \" 12321\""));
        }
    }

    /** Test bad port arg 0.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testBadPortArgs0() throws IOException, InterruptedException {
        String[] args = new String[] {"-p0"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[ApexEditorParameters: URI=http://0.0.0.0:0/apexservices/, TTL=-1sec], "
                    + "State=STOPPED) parameters invalid, port must be between 1024 and 65535"));
        }
    }

    /** Test bad port arg 1023.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testBadPortArgs1023() throws IOException, InterruptedException {
        String[] args = new String[] {"-p1023"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[ApexEditorParameters: URI=http://0.0.0.0:1023/apexservices/, TTL=-1sec], "
                    + "State=STOPPED) parameters invalid, port must be between 1024 and 65535"));
        }
    }

    /** Test bad port arg 65536.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testBadPortArgs65536() throws IOException, InterruptedException {
        String[] args = new String[] {"-p65536"};

        try {
            runEditor(args);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertTrue(e.getMessage().startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                    + "Config=[ApexEditorParameters: URI=http://0.0.0.0:65536/apexservices/, TTL=-1sec], "
                    + "State=STOPPED) parameters invalid, port must be between 1024 and 65535"));
        }
    }

    /** Test TTL arg 0.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testTTLArg0() throws IOException, InterruptedException {
        String[] args = new String[] {"-t10"};

        String outString = runEditor(args);

        assertTrue(outString.startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:18989/apexservices/, TTL=10sec], "
                + "State=READY) starting at http://0.0.0.0:18989/apexservices/"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").contains("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:18989/apexservices/, TTL=10sec], State=RUNNING) started"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").endsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:18989/apexservices/, TTL=10sec], State=STOPPED) shut down "));
    }

    /** Test TTL arg 10.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testTTLArg1() throws IOException, InterruptedException {
        String[] args = new String[] {"-t", "10", "-l", "localhost"};

        String outString = runEditor(args);

        assertTrue(outString.startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://localhost:18989/apexservices/, TTL=10sec], "
                + "State=READY) starting at http://localhost:18989/apexservices/"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").contains("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://localhost:18989/apexservices/, TTL=10sec], State=RUNNING) started"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").endsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://localhost:18989/apexservices/, TTL=10sec], State=STOPPED) shut down "));
    }

    /** Test port TTL arg 0.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testPortTTLArg0() throws IOException, InterruptedException {
        String[] args = new String[] {"-t", "10", "-p", "12321"};

        String outString = runEditor(args);

        assertTrue(outString.startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=10sec], "
                + "State=READY) starting at http://0.0.0.0:12321/apexservices/"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").contains("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=10sec], State=RUNNING) started"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").endsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://0.0.0.0:12321/apexservices/, TTL=10sec], State=STOPPED) shut down "));
    }


    /** Test port TTL arg 10.
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException if the test is interrupted
     */
    @Test
    public void testPortTTLArg1() throws IOException, InterruptedException {
        String[] args = new String[] {"--time-to-live", "10", "--port", "12321", "--listen", "127.0.0.1"};

        String outString = runEditor(args);

        assertTrue(outString.startsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://127.0.0.1:12321/apexservices/, TTL=10sec], "
                + "State=READY) starting at http://127.0.0.1:12321/apexservices/"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").contains("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://127.0.0.1:12321/apexservices/, TTL=10sec], State=RUNNING) started"));
        assertTrue(outString.replaceAll("[\\r?\\n]+", " ").endsWith("Apex Editor REST endpoint (ApexEditorMain: "
                + "Config=[ApexEditorParameters: URI=http://127.0.0.1:12321/apexservices/, TTL=10sec], State=STOPPED) shut down "));
    }

    /** Run the editor for tests.
     * @param args the args
     * @return the output string
     * @throws InterruptedException if the test is interrupted
     */
    private String runEditor(final String[] args) throws InterruptedException {
        ByteArrayOutputStream outBAStream = new ByteArrayOutputStream();
        PrintStream outStream = new PrintStream(outBAStream);

        final ApexEditorMain editorMain = new ApexEditorMain(args, outStream);

        // This test must be started in a thread because we want to intercept the output in cases where the editor is started infinitely
        Runnable testThread = new Runnable() {
            @Override
            public void run() {
                editorMain.init();
            }
        };
        new Thread(testThread).start();
        while (editorMain.getState().equals(EditorState.READY) || editorMain.getState().equals(EditorState.INITIALIZING)) {
            Thread.sleep(100);
        }

        editorMain.shutdown();
        String outString = outBAStream.toString();
        System.out.println(outString);
        return outString;
    }
}

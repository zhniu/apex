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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.HeaderDelimitedTextBlockReader;
import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.TextBlock;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestXMLTaggedEventConsumer {
    @Test
    public void testGarbageTextLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("hello there".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertNull(textBlock.getText());
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testPartialEventLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("1469781869268</TestTimestamp></MainTag>".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertNull(textBlock.getText());
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>");
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageBeforeLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("Garbage<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageBeforeAfterLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("Garbage<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>Rubbish".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageAfterLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>Rubbish".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>Rubbish");
        assertTrue(textBlock.isEndOfText());
    }
    
    @Test
    public void testGarbageTextMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("hello\nthere".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testPartialEventMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("1469781869268\n</TestTimestamp>\n</MainTag>".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\n\n".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>");
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageBeforeMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("Garbage\n<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\n\n".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>");
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageBeforeAfterMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("Garbage\n<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRubbish\n\n".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRubbish");
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageAfterMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRubbish".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRubbish");
        assertTrue(textBlock.isEndOfText());
    }
    
    @Test
    public void testPartialEventsLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("1469781869268</TestTimestamp></MainTag><?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp>".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventsGarbageBeforeLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("Garbage<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag><?xml><MainTag><TestTimestamp>".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventsGarbageBeforeAfterLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("Garbage<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>Rubbish<?xml><MainTag><TestTimestamp>\nRefuse".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventsGarbageAfterLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>Rubbish<?xml><MainTag><TestTimestamp>Refuse".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml><MainTag><TestTimestamp>1469781869268</TestTimestamp></MainTag>Rubbish<?xml><MainTag><TestTimestamp>Refuse");
        assertTrue(textBlock.isEndOfText());
    }
    
    @Test
    public void testPartialEventsMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("1469781869268\n</TestTimestamp>\n</MainTag>\n<?xml>\n<MainTag>\n<TestTimestamp>".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>");
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventsMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\n<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\n".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>");
        assertFalse(textBlock.isEndOfText());

        textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>");
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventsGarbageBeforeMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("Garbage\n<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\n\n<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\n".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>");
        assertFalse(textBlock.isEndOfText());

        textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>");
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventsGarbageBeforeAfterMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("Garbage\n<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRubbish\n<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRefuse\n".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRubbish");
        assertFalse(textBlock.isEndOfText());

        textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRefuse");
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventsGarbageAfterMultiLine() throws IOException {
        InputStream xmlInputStream = new ByteArrayInputStream("<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRubbish".getBytes());

        HeaderDelimitedTextBlockReader xmlTaggedReader = new HeaderDelimitedTextBlockReader("<?xml");
        xmlTaggedReader.init(xmlInputStream);

        TextBlock textBlock = xmlTaggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "<?xml>\n<MainTag>\n<TestTimestamp>1469781869268</TestTimestamp>\n</MainTag>\nRubbish");
        assertTrue(textBlock.isEndOfText());
    }
}

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

import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.CharacterDelimitedTextBlockReader;
import com.ericsson.apex.service.engine.event.impl.filecarrierplugin.consumer.TextBlock;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestJSONTaggedEventConsumer {

    @Test
    public void testGarbageText() throws IOException {
        InputStream jsonInputStream = new ByteArrayInputStream("hello there".getBytes());

        CharacterDelimitedTextBlockReader taggedReader = new CharacterDelimitedTextBlockReader('{', '}');
        taggedReader.init(jsonInputStream);

        TextBlock textBlock = taggedReader.readTextBlock();
        assertNull(textBlock.getText());
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testPartialEvent() throws IOException {
        InputStream jsonInputStream = new ByteArrayInputStream("\"TestTimestamp\": 1469781869268}".getBytes());

        CharacterDelimitedTextBlockReader taggedReader = new CharacterDelimitedTextBlockReader('{', '}');
        taggedReader.init(jsonInputStream);

        TextBlock textBlock = taggedReader.readTextBlock();
        assertNull(textBlock.getText());
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEvent() throws IOException {
        InputStream jsonInputStream = new ByteArrayInputStream("{TestTimestamp\": 1469781869268}".getBytes());

        CharacterDelimitedTextBlockReader taggedReader = new CharacterDelimitedTextBlockReader('{', '}');
        taggedReader.init(jsonInputStream);

        TextBlock textBlock = taggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "{TestTimestamp\": 1469781869268}");

        textBlock = taggedReader.readTextBlock();
        assertNull(textBlock.getText());
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageBefore() throws IOException {
        InputStream jsonInputStream = new ByteArrayInputStream("Garbage{TestTimestamp\": 1469781869268}".getBytes());

        CharacterDelimitedTextBlockReader taggedReader = new CharacterDelimitedTextBlockReader('{', '}');
        taggedReader.init(jsonInputStream);

        TextBlock textBlock = taggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "{TestTimestamp\": 1469781869268}");
        assertFalse(textBlock.isEndOfText());

        textBlock = taggedReader.readTextBlock();
        assertNull(textBlock.getText());
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageBeforeAfter() throws IOException {
        InputStream jsonInputStream = new ByteArrayInputStream("Garbage{TestTimestamp\": 1469781869268}Rubbish".getBytes());

        CharacterDelimitedTextBlockReader taggedReader = new CharacterDelimitedTextBlockReader('{', '}');
        taggedReader.init(jsonInputStream);

        TextBlock textBlock = taggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "{TestTimestamp\": 1469781869268}");
        assertFalse(textBlock.isEndOfText());

        textBlock = taggedReader.readTextBlock();
        assertNull(textBlock.getText());
        assertTrue(textBlock.isEndOfText());
    }

    @Test
    public void testFullEventGarbageAfter() throws IOException {
        InputStream jsonInputStream = new ByteArrayInputStream("{TestTimestamp\": 1469781869268}Rubbish".getBytes());

        CharacterDelimitedTextBlockReader taggedReader = new CharacterDelimitedTextBlockReader('{', '}');
        taggedReader.init(jsonInputStream);

        TextBlock textBlock = taggedReader.readTextBlock();
        assertEquals(textBlock.getText(), "{TestTimestamp\": 1469781869268}");
        assertFalse(textBlock.isEndOfText());

        textBlock = taggedReader.readTextBlock();
        assertNull(textBlock.getText());
        assertTrue(textBlock.isEndOfText());
    }
}

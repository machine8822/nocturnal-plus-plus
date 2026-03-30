package com.model;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for Comment bug #68
 */
public class CommentTest {

    // Tests for Bug #68

    @Test
    public void comment_edit_withValidText_updatesText() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("Updated text");
        assertEquals("Updated text", c.getText());
    }

    @Test
    public void comment_edit_withValidText_setsIsEdited() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("Updated text");
        assertTrue(c.isEdited());
    }

    @Test
    public void comment_edit_withNullText_storesEmptyString() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit(null);
        assertEquals("", c.getText());
    }

    @Test
    public void comment_edit_withBlankText_storesBlankText() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("   ");
        assertEquals("Bug #68: blank whitespace should not be accepted as valid edit text",
                "   ", c.getText());
    }

    @Test
    public void comment_edit_withBlankText_setsIsEdited() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("   ");
        assertTrue("Bug #68: isEdited is set to true even for blank text",
                c.isEdited());
    }

    @Test
    public void comment_edit_updatesTimestamp() throws InterruptedException {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        java.time.LocalDateTime before = c.getTimestamp();
        Thread.sleep(10);
        c.edit("Updated text");
        assertTrue(c.getTimestamp().isAfter(before));
    }
}

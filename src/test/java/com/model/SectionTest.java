package com.model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class SectionTest {

    // Bug #71 — string constructor crashes on null/invalid sectionType

    @Test(expected = NullPointerException.class)
    public void section_stringConstructor_nullSectionType_throwsNPE() {
        new Section("title", "body", (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void section_stringConstructor_invalidSectionType_throwsIAE() {
        new Section("title", "body", "NOT_A_REAL_TYPE");
    }

    @Test
    public void section_stringConstructor_validSectionType_works() {
        Section s = new Section("title", "body", "PROBLEM");
        assertEquals(SectionType.PROBLEM, s.getSectionType());
    }

    // Bug #72 — setMaxLinesOfCode/setTimeLimitSeconds accept negative values

    @Test
    public void section_setMaxLinesOfCode_negativeValue_isStored() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setMaxLinesOfCode(-5);
        assertEquals(Integer.valueOf(-5), s.getMaxLinesOfCode());
    }

    @Test
    public void section_setTimeLimitSeconds_negativeValue_isStored() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setTimeLimitSeconds(-10);
        assertEquals(Integer.valueOf(-10), s.getTimeLimitSeconds());
    }

    @Test
    public void section_setMaxLinesOfCode_positiveValue_works() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setMaxLinesOfCode(20);
        assertEquals(Integer.valueOf(20), s.getMaxLinesOfCode());
    }

    @Test
    public void section_setTimeLimitSeconds_positiveValue_works() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setTimeLimitSeconds(30);
        assertEquals(Integer.valueOf(30), s.getTimeLimitSeconds());
    }

    // Constructor

    @Test
    public void constructor_nullTitle_defaultsToEmpty() {
        Section s = new Section(null, "body", DataType.STRING, SectionType.PROBLEM);
        assertEquals("", s.getTitle());
    }

    @Test
    public void constructor_nullBody_defaultsToEmpty() {
        Section s = new Section("title", null, DataType.STRING, SectionType.PROBLEM);
        assertEquals("", s.getBody());
    }

    @Test
    public void constructor_nullSectionType_defaultsToDescription() {
        Section s = new Section("title", "body", DataType.STRING, null);
        assertEquals(SectionType.DESCRIPTION, s.getSectionType());
    }

    // addConstraint / setConstraints

    @Test
    public void addConstraint_valid_addsConstraint() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addConstraint("Must be O(n)");
        assertEquals(1, s.getConstraints().size());
    }

    @Test
    public void addConstraint_null_doesNotAdd() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addConstraint(null);
        assertTrue(s.getConstraints().isEmpty());
    }

    @Test
    public void addConstraint_blank_doesNotAdd() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addConstraint("   ");
        assertTrue(s.getConstraints().isEmpty());
    }

    @Test
    public void setConstraints_filtersNullsAndBlanks() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        List<String> constraints = new ArrayList<>();
        constraints.add("Valid");
        constraints.add(null);
        constraints.add("   ");
        constraints.add("Also valid");
        s.setConstraints(constraints);
        assertEquals(2, s.getConstraints().size());
    }

    @Test
    public void setConstraints_null_clearsConstraints() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addConstraint("existing");
        s.setConstraints(null);
        assertTrue(s.getConstraints().isEmpty());
    }

    // addExample / setExamples

    @Test
    public void addExample_valid_addsExample() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addExample("Input: [1,2] Output: 3");
        assertEquals(1, s.getExamples().size());
    }

    @Test
    public void addExample_null_doesNotAdd() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addExample(null);
        assertTrue(s.getExamples().isEmpty());
    }

    @Test
    public void addExample_blank_doesNotAdd() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addExample("   ");
        assertTrue(s.getExamples().isEmpty());
    }

    @Test
    public void setExamples_filtersNullsAndBlanks() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        List<String> examples = new ArrayList<>();
        examples.add("Valid");
        examples.add(null);
        examples.add("   ");
        s.setExamples(examples);
        assertEquals(1, s.getExamples().size());
    }

    @Test
    public void setExamples_null_clearsExamples() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addExample("existing");
        s.setExamples(null);
        assertTrue(s.getExamples().isEmpty());
    }

    // addAnswer

    @Test
    public void addAnswer_valid_addsAnswer() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addAnswer(new Answer("code", "exp", java.util.UUID.randomUUID()));
        assertEquals(1, s.getAnswers().size());
    }

    @Test
    public void addAnswer_null_doesNotAdd() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addAnswer(null);
        assertTrue(s.getAnswers().isEmpty());
    }

    // addComment

    @Test
    public void addComment_valid_addsComment() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addComment(new Comment("text", java.util.UUID.randomUUID()));
        assertEquals(1, s.getComments().size());
    }

    @Test
    public void addComment_null_doesNotAdd() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.addComment(null);
        assertTrue(s.getComments().isEmpty());
    }

    // deleteComment

    @Test
    public void deleteComment_byAuthor_returnsTrue() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        Comment c = new Comment("text", authorId);
        s.addComment(c);
        assertTrue(s.deleteComment(c.getCommentId(), authorId, false));
    }

    @Test
    public void deleteComment_byNonAuthor_returnsFalse() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        s.addComment(c);
        assertFalse(s.deleteComment(c.getCommentId(), java.util.UUID.randomUUID(), false));
    }

    @Test
    public void deleteComment_byAdmin_returnsTrue() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        s.addComment(c);
        assertTrue(s.deleteComment(c.getCommentId(), java.util.UUID.randomUUID(), true));
    }

    @Test
    public void deleteComment_nullCommentId_returnsFalse() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        assertFalse(s.deleteComment(null, java.util.UUID.randomUUID(), false));
    }

    @Test
    public void deleteComment_insideAnswer_returnsTrue() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        Comment c = new Comment("answer comment", authorId);
        a.addComment(c);
        s.addAnswer(a);
        assertTrue(s.deleteComment(c.getCommentId(), authorId, false));
    }

    // deleteAnswer

    @Test
    public void deleteAnswer_byAuthor_returnsTrue() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        Answer a = new Answer("code", "exp", authorId);
        s.addAnswer(a);
        assertTrue(s.deleteAnswer(a.getAnswerId(), authorId, false));
        assertTrue(s.getAnswers().isEmpty());
    }

    @Test
    public void deleteAnswer_byNonAuthor_returnsFalse() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        s.addAnswer(a);
        assertFalse(s.deleteAnswer(a.getAnswerId(), java.util.UUID.randomUUID(), false));
    }

    @Test
    public void deleteAnswer_byAdmin_returnsTrue() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        s.addAnswer(a);
        assertTrue(s.deleteAnswer(a.getAnswerId(), java.util.UUID.randomUUID(), true));
    }

    @Test
    public void deleteAnswer_nullAnswerId_returnsFalse() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        assertFalse(s.deleteAnswer(null, java.util.UUID.randomUUID(), false));
    }

    @Test
    public void deleteAnswer_nonExistent_returnsFalse() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        assertFalse(s.deleteAnswer(java.util.UUID.randomUUID(), java.util.UUID.randomUUID(), true));
    }

    // Setters

    @Test
    public void setImageURL_null_setsEmpty() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setImageURL(null);
        assertEquals("", s.getImageURL());
    }

    @Test
    public void setImageURL_valid_setsUrl() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setImageURL("https://img.example.com/pic.png");
        assertEquals("https://img.example.com/pic.png", s.getImageURL());
    }

    @Test
    public void setExpectedTimeComplexity_null_setsEmpty() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setExpectedTimeComplexity(null);
        assertEquals("", s.getExpectedTimeComplexity());
    }

    @Test
    public void setExpectedTimeComplexity_valid_setsValue() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setExpectedTimeComplexity("O(n log n)");
        assertEquals("O(n log n)", s.getExpectedTimeComplexity());
    }

    @Test
    public void setMaxLinesOfCode_null_setsNull() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setMaxLinesOfCode(10);
        s.setMaxLinesOfCode(null);
        assertNull(s.getMaxLinesOfCode());
    }

    @Test
    public void setTimeLimitSeconds_null_setsNull() {
        Section s = new Section("t", "b", DataType.STRING, SectionType.PROBLEM);
        s.setTimeLimitSeconds(60);
        s.setTimeLimitSeconds(null);
        assertNull(s.getTimeLimitSeconds());
    }
}

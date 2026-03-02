package com.model;

import java.util.ArrayList;
import java.util.List;

public class Section {

    private String title;
    private String body;
    private SectionType sectionType;
    private DataType dataType;
    private List<Answer> answers;
    private List<Comment> comments;

    public Section(String title, String body, DataType dataType, SectionType sectionType)
    {
        this.title = title;
        this.body = body;
        this.dataType = dataType;
        this.sectionType = sectionType;
        this.answers = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    /**
     * Convenience constructor used by DataLoader (string-based section type, no data type).
     */
    public Section(String title, String body, String sectionType)
    {
        this(title, body, null, SectionType.valueOf(sectionType));
    }

    public String getTitle()
    {
        return title;
    }

    public String getBody()
    {
        return body;
    }

    public SectionType getSectionType()
    {
        return sectionType;
    }

    public DataType getDataType()
    {
        return dataType;
    }

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public void addAnswer(Answer answer)
    {
        if (answer != null)
        {
            answers.add(answer);
        }
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public void addComment(Comment comment)
    {
        if (comment != null)
        {
            comments.add(comment);
        }
    }
}

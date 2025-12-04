package com.example.m_hike_app.data;

public class Observation {
    private int id;
    private int hikeId;
    private String text;
    private String timestamp;
    private String comment;

    public Observation() {}

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getHikeId() { return hikeId; }
    public void setHikeId(int hikeId) { this.hikeId = hikeId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}

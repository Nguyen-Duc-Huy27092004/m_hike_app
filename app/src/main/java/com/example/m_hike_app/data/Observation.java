package com.example.m_hike_app.data;

public class Observation {
    private int id;
    private int hikeId;
    private String observation;
    private String time;
    private String comment;

    public Observation() {}

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getHikeId() { return hikeId; }
    public void setHikeId(int hikeId) { this.hikeId = hikeId; }

    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}

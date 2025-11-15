package com.example.m_hike_app.data;

public class Hike {
    private int id;
    private String name;
    private String location;
    private String date;
    private String parking;
    private double length;
    private String difficulty;
    private String description;

    public Hike() {}

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getParking() { return parking; }
    public void setParking(String parking) { this.parking = parking; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

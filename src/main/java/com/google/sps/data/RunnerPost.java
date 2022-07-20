package com.google.sps.data;

import com.google.cloud.Timestamp;

public class RunnerPost {
    private final String username;
    private final double distance;
    private final double time;
    private final long avgBPM;
    private final String description;
    private final Timestamp timestamp;
    private final String UID;
  
    public RunnerPost(String username, double distance, double time, long avgBPM, String description, Timestamp timestamp,String UID) {
      this.username = username;
      this.distance = distance;
      this.avgBPM = avgBPM;
      this.description = description;
      this.time = time;
      this.timestamp = timestamp;
      this.UID = UID;
    }
    public String getUsername() {
        return username;
    }
    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }
    public long getAvgBPM() {
        return avgBPM;
    }
    public String getDescription() {
        return description;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public String getUID() {
        return UID;
    }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package events;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author jamal
 */
public class GlucoseReading {
    private double level;
    private long timestamp;

    public GlucoseReading(double level) {
        this.level = level;
        this.timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public double getLevel() {
        return level;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
}
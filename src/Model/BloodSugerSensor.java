/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import esper.Config;
import events.GlucoseReading;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author jamal
 */
public class BloodSugerSensor extends Thread {
    
    

        double[] readings = {
            100, 110, 130, 170, 220, 
            210, 190, 160,         
            150, 160, 180, 220       
        };
    private int index = 0;
    private double previousReading; 
    private List<Double> readingHistory = new ArrayList<>();  

    public BloodSugerSensor(double previousReading, List<Double> readingHistory) {
        this.previousReading = previousReading;
        this.readingHistory = readingHistory;
    }

    public BloodSugerSensor() {
    }
    

    @Override
    public void run(){
        
    while (true) {


             if (index >= readings.length) {
                index = 0;
            }

            double level = readings[index];

            readingHistory.add(level);
            double change = level - previousReading;
            System.out.println("Sensor reading: " + level +
                               " | Change: " + change);
            Config.sendEvent(new GlucoseReading(level));
            previousReading = level;
            index++;
            
                    try {
            Thread.sleep(20000);
        } catch (InterruptedException ex) {
            System.getLogger(BloodSugerSensor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    }
    
    public boolean sugarSensorTest(){
        return true;
    }
    
    
}

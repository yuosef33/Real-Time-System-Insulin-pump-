/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jamal
 */
public class InsulinePump {
    private Double MaxDailyDose;
    private Double cumulativeDose;
    private List<DoseRecord> dailyDoseHistory= new ArrayList<>();

    public InsulinePump(double MaxDailyDose) {
        this.MaxDailyDose = MaxDailyDose;
        this.cumulativeDose = 0.0;
    }
    
    
    
    public boolean pumpinsuline(double dose){
        double newCumulativeDose=cumulativeDose+dose;
        if(newCumulativeDose<MaxDailyDose){
            System.out.println("Pump Insuline ...");
            cumulativeDose=newCumulativeDose;
            dailyDoseHistory.add(new DoseRecord(dose,LocalDateTime.now()));
            return true;
        }
        else{
            System.out.println("cant pump any more doses today");
            return false;
        }
    }
    
    public void resetDailyDose() {
        cumulativeDose = 0.0;
        dailyDoseHistory.clear();
        System.out.println("=== Daily dose reset ===");
    }

    public Double getCumulativeDose() {
        return cumulativeDose;
    }

    public List<DoseRecord> getDailyDoseHistory() {
        return dailyDoseHistory;
    }

    public Double getMaxDailyDose() {
        return MaxDailyDose;
    }
    public boolean insulinePumpTest(){
        if(MaxDailyDose<200)
        return true;
        return false;
    }
    
}

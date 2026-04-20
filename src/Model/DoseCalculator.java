/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author jamal
 */
public class DoseCalculator {
    
    private Double safeMin = 70.0;
    private Double safeMax = 180.0;
    private Double maxDose = 10.0; 

    public DoseCalculator(double safeMin) {
        this.safeMin = safeMin;
    }
    
    public double computeInsulineDose(double currentLevel, double previousLevel){
        
        double change = currentLevel - previousLevel;

        if (currentLevel < safeMin) {
            return 0;
        }
        if (currentLevel >= safeMin && currentLevel <= safeMax) {
            if (change > 0) {
                return calculateDose(currentLevel);
            }
            return 0;
        }
        if (currentLevel > safeMax) {
            if (change < 0) {
                return 0;
            }
            return calculateDose(currentLevel);
        }

        return 0;
    }

    private double calculateDose(double level) {

        double dose = (level - 100) / 50;
        Double newdose=Math.min(dose, maxDose);
        return newdose.floatValue();
        
    }

    public Double getSafeMin() {
        return safeMin;
    }

    public Double getSafeMax() {
        return safeMax;
    }

    public Double getMaxDose() {
        return maxDose;
    }
    
    
    
    
}

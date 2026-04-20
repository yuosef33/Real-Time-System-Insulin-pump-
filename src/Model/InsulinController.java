/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.BloodSugarView;
import View.DashboardView;
import View.DoseDeliveryView;
import View.HomeUI;
import java.awt.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jamal
 */
public class InsulinController {

    private  List<Double> readingsHistory = new ArrayList<>();
    private  List<Double> ratesHistory = new ArrayList<>();
    private DoseCalculator calculator = new DoseCalculator(70);
    private InsulinePump insulinePump;
    private InsulineReservoirSensor insulineReservoirSensor;   
    private DashboardView dashboardView;
    private BloodSugarView bloodSugerView;
    private DoseDeliveryView doseDeliveryView;
    private MessageDisplayManager messageDisplayManager;
    

    public InsulinController(InsulinePump insulinePump, InsulineReservoirSensor insulineReservoirSensor) {
        this.insulinePump = insulinePump;
        this.insulineReservoirSensor = insulineReservoirSensor;
    }
    
    public void setListener(DashboardView dashboardView,BloodSugarView bloodSugerView,DoseDeliveryView doseDeliveryView,MessageDisplayManager manager,SelfTestThread selfTestThread) {
    this.bloodSugerView=bloodSugerView;
    this.doseDeliveryView=doseDeliveryView;
    this.dashboardView=dashboardView;
    this.messageDisplayManager=manager;

    
    dashboardView.getRefillReservoirBtn().addActionListener(e -> replaceReservoir());
    dashboardView.getOpenDoseBtn().addActionListener(e -> clickDoseBtn());
    dashboardView.getOpenBloodSugarBtn().addActionListener(e -> clickBloodSugerViewBtn());
    dashboardView.setSystemStatus("Active");
    doseDeliveryView.getBackToDashboardBtn().addActionListener(e -> clickBacktoDashboardBtn(doseDeliveryView));
    bloodSugerView.getBackToDashboardBtn().addActionListener(e -> clickBacktoDashboardBtn(bloodSugerView));
    }

    public void onGlucose(Double currentLevel) {
        readingsHistory.add(currentLevel);

        if(readingsHistory.size()==1){
            dashboardView.addMessage("START MEASURING GLUCOSE");
        }
        dashboardView.setBloodSugar(currentLevel);
        bloodSugerView.setCurrentReadingField(currentLevel.floatValue());
        
        dashboardView.setCycleStatusField("Runinng");
        int size = readingsHistory.size();

        Double current = currentLevel;
        Double previous = size >= 2 ? readingsHistory.get(size - 2) : current;
        
        Double rate = current - previous;
        ratesHistory.add(rate);

        boolean isRising = rate > 0;
        boolean isFalling = rate < 0;

        boolean isAcceleratingUp = false;
        boolean isAcceleratingDown = false;
        
        
        bloodSugerView.setPreviousReadingField(previous.floatValue());
        if(isRising)
        bloodSugerView.setTrendStatusField("Rising");
        else if(isFalling)
            bloodSugerView.setTrendStatusField("Falling");
        else
            bloodSugerView.setTrendStatusField("Stable");

        
        // acceleration logic
        if (ratesHistory.size() >= 2) {
            double rate1 = ratesHistory.get(ratesHistory.size() - 2);
            double rate2 = ratesHistory.get(ratesHistory.size() - 1);

            if (rate2 > 0 && rate2 > rate1) {
                isAcceleratingUp = true;
            }

            if (rate2 < 0 && rate2 < rate1) {
                isAcceleratingDown = true;
            }
        }
        
        if(isAcceleratingUp)
            bloodSugerView.setRateTrendField("Accelerating Up");
        else if(isAcceleratingDown)
            bloodSugerView.setRateTrendField("Accelerating Down");
        else
            bloodSugerView.setRateTrendField("Stable");
        LocalDateTime now =  LocalDateTime.now();
        
        bloodSugerView.setReadingHistoryArea("reading : " + currentLevel +  " , time : " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"\n" );

   
        Double dose = calculator.computeInsulineDose(current, previous);
        // DECISION ==================

        //  low
        if (current < 70) {
            System.out.println("LOW NO INSULIN");
            dashboardView.addMessage("LOW NO INSULIN");
        }

        // safe
        else if (current <= 180) {

            if (isAcceleratingUp) {
                System.out.println(" SAFE BUT ACCELERATING UP GIVE DOSE: " + dose.floatValue());
                dashboardView.addMessage(" SAFE BUT ACCELERATING UP GIVE DOSE: " + dose.floatValue());
                pumpInsuline(dose,insulineReservoirSensor.getCapacity());
                
                
            }
            else if (isRising) {
                System.out.println("SAFE BUT RISING ");
                dashboardView.addMessage("SAFE BUT RISING ");
            }
            else if (isAcceleratingDown) {
                System.out.println(" SAFE AND DROPPING FAST NO INSULIN");
                 dashboardView.addMessage(" SAFE AND DROPPING FAST NO INSULIN");
            }
            else {
                System.out.println(" SAFE NO ACTION");
                dashboardView.addMessage(" SAFE NO ACTION");
            }
        }

        // high
        else {

            if (isAcceleratingDown) {
                System.out.println("HIGH BUT DROPPING FAST NO INSULIN");
                 dashboardView.addMessage("HIGH BUT DROPPING FAST NO INSULIN");
            }
            else if (isFalling) {
                System.out.println("HIGH BUT FALLING");
                dashboardView.addMessage("HIGH BUT FALLING");
            }
            else {
                System.out.println("HIGH GIVE INSULIN: " + dose.floatValue());
                dashboardView.addMessage("HIGH GIVE INSULIN: " + dose.floatValue());
                pumpInsuline(dose,insulineReservoirSensor.getCapacity());

            }
        }

        System.out.println("==================================================");
        dashboardView.setCycleStatusField("Waiting");
    }
    
    private void pumpInsuline(Double dose,double reservoirCapacity){
          if(reservoirCapacity>=dose){
                boolean dosePumbed =insulinePump.pumpinsuline(dose);
                
                if(dosePumbed){
                    insulineReservoirSensor.setCapacity(insulineReservoirSensor.getCapacity()-dose.floatValue());
                    dashboardView.setReservoir(insulineReservoirSensor.getCapacity().floatValue());
                    dashboardView.addMessage(dose.floatValue()+" dose pumped succesfully");
                    dashboardView.setLastDose(dose);
                    dashboardView.setCumulativeDose(insulinePump.getCumulativeDose());
                    doseDeliveryView.setComputedDoseField(dose.floatValue());
                    doseDeliveryView.setCumulativeDailyDoseField(insulinePump.getCumulativeDose().floatValue());
                    doseDeliveryView.setDoseDeliveryStatus("delivered");
                    messageDisplayManager.addMessage("dose "+dose.floatValue()+" pumped");

                }
                }else
                {
                    System.out.println("please replace the reservoir");
                    dashboardView.addMessage("please replace the reservoir");
                    doseDeliveryView.setDoseDeliveryStatus("not delivered");
                }
    }
    public void dailyReset(){
        insulinePump.resetDailyDose();
        updateCumulativeView();
    }
    
    public void replaceReservoir(){
        insulineReservoirSensor.replaceReservoir(insulinePump);
        dashboardView.setReservoir(insulineReservoirSensor.getCapacity().floatValue());
        dashboardView.setCumulativeDose(insulinePump.getCumulativeDose());
        doseDeliveryView.setCumulativeDailyDoseField(insulinePump.getCumulativeDose().floatValue());
    }
    public void clickDoseBtn(){
        doseDeliveryView.setMaxDailyDoseField(insulinePump.getMaxDailyDose().floatValue());
        doseDeliveryView.setMaxSingleDoseField(calculator.getMaxDose().floatValue());
        doseDeliveryView.setVisible(true);
        dashboardView.setVisible(false);
    }
    public void clickBacktoDashboardBtn(Component  component){
        dashboardView.setVisible(true);
        component.setVisible(false);
    }    
    public void clickBloodSugerViewBtn(){
        bloodSugerView.setVisible(true);
        dashboardView.setVisible(false);
    }
    public void updateCumulativeView(){
        dashboardView.setCumulativeDose(insulinePump.getCumulativeDose());
        doseDeliveryView.setCumulativeDailyDoseField(insulinePump.getCumulativeDose().floatValue());
    }
    

    
}
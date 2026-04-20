/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package esper;

import Model.BloodSugerSensor;
import Model.ClockThread;
import Model.InsulinController;
import Model.InsulinePump;
import Model.InsulineReservoirSensor;
import Model.MessageDisplayManager;
import Model.SelfTestThread;
import View.BloodSugarView;
import View.DashboardView;
import View.DoseDeliveryView;
import View.HomeUI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author jamal
 */
public class Main {
     public static void main(String[] args) throws InterruptedException {
        final InsulinePump insulinePump=new InsulinePump(100);
        final InsulineReservoirSensor insulineReservoirSensor =new InsulineReservoirSensor(100.0f);
        final InsulinController insulinController = new InsulinController(insulinePump,insulineReservoirSensor);
        BloodSugerSensor sensor = new BloodSugerSensor();
        DashboardView dashboardView= new DashboardView();
        DoseDeliveryView doseDeliveryView= new DoseDeliveryView();
        BloodSugarView bloodSugarView = new  BloodSugarView();
        MessageDisplayManager manager = new MessageDisplayManager(dashboardView);
        SelfTestThread selfTestThread= new SelfTestThread(insulineReservoirSensor, sensor, insulinePump, manager);
        insulinController.setListener(dashboardView,bloodSugarView,doseDeliveryView,manager,selfTestThread);
        ClockThread clock = new ClockThread(dashboardView,insulinController);
        clock.start();
        dashboardView.setVisible(true);

        
        // Disable logging
        Logger.getRootLogger().setLevel(Level.OFF);

        // Register events
        Config.registerEvents();

        Config.createStatement("select level,timestamp from GlucoseReading").setSubscriber(new Object () {
            public void update(double level ,long timestamp){

                    LocalDateTime time = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(timestamp),
                        ZoneId.systemDefault()
                        );
              System.out.println("new level reading:"+level+
                      " ,Date :"+time.toLocalDate() +
                      " ,time :"+time.toLocalTime());    
              
             insulinController.onGlucose(level);
            }});
        
        
        sensor.start();

      
     }

}

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
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author jamal
 */
public class Main {
     public static void main(String[] args) throws InterruptedException {
         
            InsulinePump pump = new InsulinePump(100);
            InsulineReservoirSensor reservoir = new InsulineReservoirSensor(100.0f);


            DashboardView dashboard = new DashboardView();
            DoseDeliveryView doseView = new DoseDeliveryView();
            BloodSugarView sugarView = new BloodSugarView();

            BloodSugerSensor sensor = new BloodSugerSensor();
            MessageDisplayManager messageManager = new MessageDisplayManager(dashboard);

            SelfTestThread selfTest = new SelfTestThread(
                    reservoir,
                    sensor,
                    pump,
                    dashboard
            );


            InsulinController controller =
                    new InsulinController(pump, reservoir);

            controller.setListener(
                    dashboard,
                    sugarView,
                    doseView,
                    messageManager,
                    selfTest
            );


            ClockThread clock = new ClockThread(dashboard, controller);
            clock.start();


            Logger.getRootLogger().setLevel(Level.OFF);
            Config.registerEvents();

            Config.createStatement("select level,timestamp from GlucoseReading")
                    .setSubscriber(new Object() {
                        public void update(double level, long timestamp) {

                            controller.onGlucose(level);
                        }
                    });
            Config.createStatement("select capacityLevel from ReservoirReading").setSubscriber(new Object() {
                        public void update(Double capacityLevel) {
                            controller.replaceReservoir(capacityLevel);
                        }
                    });
            messageManager.start();
            selfTest.start();

            dashboard.setVisible(true);
            sensor.start();
    
    }

}

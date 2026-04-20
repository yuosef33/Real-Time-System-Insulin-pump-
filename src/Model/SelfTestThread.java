/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author jamal
 */
public class SelfTestThread extends Thread {

    private InsulineReservoirSensor sensor;
    private BloodSugerSensor bloodSugerSensor;
    private InsulinePump insulinePump;
    private MessageDisplayManager messageManager;

    public SelfTestThread(InsulineReservoirSensor sensor, BloodSugerSensor bloodSugerSensor, InsulinePump insulinePump, MessageDisplayManager messageManager) {
        this.sensor = sensor;
        this.bloodSugerSensor = bloodSugerSensor;
        this.insulinePump = insulinePump;
        this.messageManager = messageManager;
    }
    



    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(30000); 
            } catch (InterruptedException e) {}

            runSelfTest();
        }
    }

    private void runSelfTest() {
        StringBuffer message=new StringBuffer();
         message.append("Self Test Start \n");
        if (sensor.ReservoirSensorTest()) {
            message.append("ReservoirSensor OK \n");
        } else {
            message.append("Reservoir Empty! \n");
        }
        if(bloodSugerSensor.sugarSensorTest()){
                       message.append("sugarSensor OK \n");
        } else {
            message.append("suger Sensor error \n");
        } 
        if(insulinePump.insulinePumpTest()){
           message.append("insulinePump OK \n");
        } else {
            message.append("max daily dose too high \n");
        } 
         message.append("Self Test Finished \n");

        messageManager.addMessage(message.toString());
        
        
    }
}
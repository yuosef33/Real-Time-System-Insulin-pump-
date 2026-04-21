
package Model;

/**
 *
 * @author jamal
 */
public class InsulineReservoirSensor {
    private Float capacity;

    public InsulineReservoirSensor(Float capacity) {
        if(capacity==100.0)
        this.capacity = capacity;
        else
          throw new RuntimeException("reservoir capacity must 100ml");
    }

    public Float getCapacity() {
        return capacity;
    }

    public void setCapacity(Float capacity) {
        this.capacity = capacity;
    }
    
    public boolean replaceReservoir(InsulinePump pump,Double capacityLevel){
        if(capacityLevel==100.0){
            capacity=100.0f;
            pump.resetDailyDose();
            System.out.println("=== System Reset ===");
            return true;
        }
        return false;
    }
    
    public boolean ReservoirSensorTest(){
        if(capacity>0)
        return true;
        return false;
    }
    
}

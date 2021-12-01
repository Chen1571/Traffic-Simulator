/**
 * This VehicleQueue class represents a queue abstract data type.
 * It is modeled as a queue of Vehicles.
 */
import java.util.ArrayList;

public class VehicleQueue extends ArrayList<Vehicle> {

    /**
     * Enqueues by adding the Vehicle to the end of the arraylist.
     * 0 index represents front, size() - 1 index represents rear.
     * @param vehicle
     *      The Vehicle to be enqueued.
     */
    public void enqueue(Vehicle vehicle) {
        add(vehicle);
    }

    /**
     * Dequeues by removing the Vehicle from the first index of the arraylist.
     * @return
     *      The Vehicle that is dequeued.
     */
    public Vehicle dequeue() {
        return remove(0);
    }

    /**
     * The string representation of this VehicleQueue class.
     * @return
     *      The string representation of this Vehicle Queue class.
     */
    public String toString() {
        String output = "";
        for (int i = 0; i < size(); i++)
            output += (get(i).getSerialId() + ", ");

        return output;
    }
}
